package com.lenovo.treasury.rule.service.impl;

import com.lenovo.ebs.common.utils.DateUtils;
import com.lenovo.ebs.common.utils.StringUtils;
import com.lenovo.ebs.tube.rule.dto.AccountNumberParam;
import com.lenovo.ebs.tube.rule.mapper.RuleExtendMapper;
import com.lenovo.ebs.tube.rule.service.EbsTransactionDataCleanService;
import com.lenovo.ebs.tube.rule.service.ISysConfigService;
//import com.lenovo.treasury.entity.dto.EccFxRateDTO;
import com.lenovo.treasury.rule.service.RuleExtendService;
//import com.lenovo.treasury.service.ExcahangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RuleExtendServiceImpl implements RuleExtendService {

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private RuleExtendMapper ruleExtendMapper;

    @Autowired
    private EbsTransactionDataCleanService ebsTransactionDataCleanService;

//    @Autowired
//    private ExcahangeRateService interestRateService;

    @Override
    public String getAccountNumberByFixedPrefix(String payNote, String configByKey, String splitStr) {
        int indexOf = payNote.indexOf(configByKey);
        String substring = "";
        if (indexOf != -1) {
            substring = payNote.substring(indexOf + configByKey.length());
        } else {
            log.error("### payNote:{} not contains prefix:{}", payNote, configByKey);
            return substring;
        }
        if (StringUtils.isBlank(substring)) {
            return substring;
        }
        String[] split = substring.split(splitStr);
        String accountNumber = split[0];
        if (StringUtils.isBlank(accountNumber)) {
            return accountNumber;
        }
        int accountNumberCount = ruleExtendMapper.getAccountNumberCount(accountNumber);
        if (accountNumberCount > 0) {
            return accountNumber;
        }

        return StringUtils.EMPTY;
    }

    @Override
    public String getAccountNumberByMatchAccount(Map<String, String> context) {
        if (MapUtils.isEmpty(context)) {
            return StringUtils.EMPTY;
        }
        String payNote = context.get("payment_note");
        String containStr = context.get("contain_str");
        String multiplicandStr = context.get("multiplicand_str");
        if (!StringUtils.containsIgnoreCase(payNote, containStr)) {
            return StringUtils.EMPTY;
        }
        List<String> needsHandleTables = ebsTransactionDataCleanService.getNeedsHandleTables();
        if (CollectionUtils.isEmpty(needsHandleTables)) {
            log.warn("### RuleExtendServiceImpl getAccountNumberByMatchAccount  not have need handle table! ###");
            return StringUtils.EMPTY;
        }
        String formCurrency = context.get("currency");
        String amount = context.get("amount");
        String foreignCurrency = context.get("foreign_currency");
        String foreignCurrencyAmt = context.get("foreign_currency_amt");
        String debitCreditKey = context.get("debit_credit_key");
        String toDebitCredit = "H";
        if (StringUtils.equalsIgnoreCase(debitCreditKey, "H")) {
            toDebitCredit = "S";
        }
        for (String table : needsHandleTables) {
            String tableName = ebsTransactionDataCleanService.getTableName(table);
            if (StringUtils.isNotBlank(foreignCurrency) && StringUtils.isNotBlank(foreignCurrencyAmt)) {
                BigDecimal fromAmount = new BigDecimal(foreignCurrencyAmt);
                String accountNumber = getAccountNumber(foreignCurrency, fromAmount, BigDecimal.ONE, multiplicandStr, tableName, toDebitCredit);
                if (StringUtils.isNotBlank(accountNumber)) {
                    return accountNumber;
                }
            }
        }

        for (String table : needsHandleTables) {
            String tableName = ebsTransactionDataCleanService.getTableName(table);
            BigDecimal fromAmount = new BigDecimal(amount);
            String accountNumber = getAccountNumber(formCurrency, fromAmount, BigDecimal.ONE, multiplicandStr, tableName, toDebitCredit);
            if (StringUtils.isNotBlank(accountNumber)) {
                return accountNumber;
            }
        }

        // USD、HKD、RMB,EUR
        List<String> currencyList = Arrays.asList("USD", "HKD", "RMB", "EUR");
        for (String table : needsHandleTables) {
            String tableName = ebsTransactionDataCleanService.getTableName(table);
            BigDecimal fromAmount = new BigDecimal(amount);
            for (String toCurrency : currencyList) {
                BigDecimal rate = BigDecimal.ONE;
                if (!StringUtils.equalsIgnoreCase(formCurrency, toCurrency)) {
//                    EccFxRateDTO transferTicket = interestRateService.findTransferTicket(formCurrency, toCurrency);
//                    if (transferTicket != null) {
//                        rate = new BigDecimal(transferTicket.getUkurs());
//                    }
                }
                String accountNumber = getAccountNumber(toCurrency, fromAmount, rate, multiplicandStr, tableName, toDebitCredit);
                if (StringUtils.isNotBlank(accountNumber)) {
                    return accountNumber;
                }
            }
        }

        return StringUtils.EMPTY;
    }

    @Override
    public String getAccountNumberByMatch(Map<String, String> context) {
        AccountNumberParam result = getAccountNumberParam(context);
        if (result == null) {
            return StringUtils.EMPTY;
        }
        if (StringUtils.isBlank(result.getCounterAccountNumber())) {
            return StringUtils.EMPTY;
        }
        return result.getCounterAccountNumber();
    }

    @Override
    public String getTicketNumberByMatch(Map<String, String> context) {
        context.put("checkTicketNumberCount", "0");
        AccountNumberParam result = getAccountNumberParam(context);
        if (result == null) {
            return StringUtils.EMPTY;
        }
        if (StringUtils.isBlank(result.getTicketNumber())) {
            return StringUtils.EMPTY;
        }
        return result.getTicketNumber();
    }

    private AccountNumberParam getAccountNumberParam(Map<String, String> context) {
        if (MapUtils.isEmpty(context)) {
            return null;
        }
        String payNote = context.get("payment_note");
        String containStr1 = context.get("contain_str1");
        String containStr2 = context.get("contain_str2");
        if (!StringUtils.containsIgnoreCase(payNote, containStr1) && !StringUtils.containsIgnoreCase(payNote, containStr2)) {
            log.warn("### RuleExtendServiceImpl getAccountNumberParam payNote:{} not contains prefix:{} or {}", payNote, containStr1, containStr2);
            return null;
        }

        String multiplicandStr = context.get("multiplicand_str");
        String currency = context.get("currency");
        String amount = context.get("amount");
        String debitCreditKey = context.get("debit_credit_key");
        String reportDate = context.get("report_date");
        String companyCode = context.get("company_code");
        String bankAccount = context.get("bank_account");
        String detailSources = context.get("detail_sources");
        String iban = context.get("iban");
        String fullAcctNumber = context.get("full_acct_number");

        if (StringUtils.equalsIgnoreCase(debitCreditKey, "S") && StringUtils.equalsIgnoreCase(detailSources, "LTP_CIT")) {
            log.warn("### RuleExtendServiceImpl getAccountNumberParam debitCreditKey:{} detailSources:{} ignore! ###", debitCreditKey, detailSources);
            return null;
        }
        Date endDate;
        try {
            endDate = DateUtils.dateParse(reportDate, "");
        } catch (ParseException e) {
            log.error("### RuleExtendServiceImpl getAccountNumberParam reportDate:{} parse error! ###", reportDate);
            return null;
        }
        Date startDate = DateUtils.addDays(endDate, -10);

        BigDecimal curAmount = new BigDecimal(amount);
        BigDecimal multiplicandDecimal = new BigDecimal(multiplicandStr);
        BigDecimal endAmount = getMultiplyAmount(curAmount, new BigDecimal("2").subtract(multiplicandDecimal));
        BigDecimal startAmount = getMultiplyAmount(curAmount, multiplicandDecimal);

        AccountNumberParam accountNumberParam = new AccountNumberParam();
        accountNumberParam.setCompanyCode(companyCode);
        accountNumberParam.setAccountNumber(bankAccount);
        accountNumberParam.setCurrency(currency);
        accountNumberParam.setStartAmount(startAmount);
        accountNumberParam.setEndAmount(endAmount);
        accountNumberParam.setBeginTime(startDate);
        accountNumberParam.setEndTime(endDate);

        AccountNumberParam result = null;
        if (StringUtils.isNotBlank(bankAccount)) {
            result = getAccountNumberParam(context, debitCreditKey, accountNumberParam, curAmount);
        }
        if (result == null && StringUtils.isNotBlank(iban)) {
            accountNumberParam.setAccountNumber(iban);
            result = getAccountNumberParam(context, debitCreditKey, accountNumberParam, curAmount);
        }
        if (result == null && StringUtils.isNotBlank(fullAcctNumber)) {
            accountNumberParam.setAccountNumber(fullAcctNumber);
            result = getAccountNumberParam(context, debitCreditKey, accountNumberParam, curAmount);
        }
        return result;
    }

    private AccountNumberParam getAccountNumberParam(Map<String, String> context, String debitCreditKey, AccountNumberParam accountNumberParam, BigDecimal curAmount) {
        List<AccountNumberParam> dbResult;
        if (StringUtils.equalsIgnoreCase(debitCreditKey, "H")) {
            dbResult = ruleExtendMapper.getAccountNumberParamByH(accountNumberParam);
        } else {
            dbResult = ruleExtendMapper.getAccountNumberParamByS(accountNumberParam);
        }
        if (CollectionUtils.isEmpty(dbResult)) {
            log.warn("### RuleExtendServiceImpl getAccountNumberParam  not have match data! ###");
            return null;
        }
        List<String> needsHandleTables = ebsTransactionDataCleanService.getNeedsHandleTables();
        if (CollectionUtils.isEmpty(needsHandleTables)) {
            log.warn("### RuleExtendServiceImpl getAccountNumberParam  not have need handle table! ###");
            return null;
        }
        String checkTicketNumberCount = context.get("checkTicketNumberCount");
        List<AccountNumberParam> result = new ArrayList<>();
        for (AccountNumberParam item : dbResult) {
            if (StringUtils.equals(checkTicketNumberCount, "0") && checkTicketNumberCount(item, needsHandleTables, debitCreditKey)) {
                continue;
            }
            BigDecimal itemAmount = item.getAmount();
            if (itemAmount == null || itemAmount.compareTo(BigDecimal.ZERO) < 0) {
                continue;
            }
            BigDecimal percent;
            if (itemAmount.compareTo(curAmount) > 0) {
                percent = curAmount.divide(itemAmount, 8, RoundingMode.HALF_UP);
            } else {
                percent = itemAmount.divide(curAmount, 8, RoundingMode.HALF_UP);
            }
            item.setPercent(percent);
            result.add(item);
        }
        if (CollectionUtils.isEmpty(result)) {
            log.warn("### RuleExtendServiceImpl getAccountNumberParam  not have match data! ###");
            return null;
        }
        result = result.stream().sorted(Comparator.comparing(AccountNumberParam::getPercent).reversed()).collect(Collectors.toList());
        return result.get(0);
    }

    private Boolean checkTicketNumberCount(AccountNumberParam item, List<String> needsHandleTables, String debitCreditKey) {
        for (String table : needsHandleTables) {
            String tableName = ebsTransactionDataCleanService.getTableName(table);
            AccountNumberParam param = new AccountNumberParam();
            param.setDebitCreditKey(debitCreditKey);
            param.setTicketNumber(item.getTicketNumber());
            param.setTableName(tableName);
            int count = ruleExtendMapper.getAccountNumberCountByParam(param);
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    public String getAccountNumber(String toCurrency, BigDecimal fromAmount, BigDecimal rate, String multiplicandStr, String tableName, String toDebitCredit) {
        BigDecimal endAmount = getMultiplyAmount(fromAmount, rate);
        BigDecimal startAmount = getMultiplyAmount(endAmount, new BigDecimal(multiplicandStr));
        AccountNumberParam accountNumberParam = new AccountNumberParam();
        accountNumberParam.setCurrency(toCurrency);
        accountNumberParam.setTableName(tableName);
        accountNumberParam.setDebitCreditKey(toDebitCredit);
        accountNumberParam.setStartAmount(startAmount);
        accountNumberParam.setEndAmount(endAmount);
        String accountNumber = ruleExtendMapper.getAccountNumber(accountNumberParam);
        if (StringUtils.isNotBlank(accountNumber)) {
            return accountNumber;
        }
        return null;
    }

    private BigDecimal getMultiplyAmount(BigDecimal fromAmount, BigDecimal multiplicand) {
        return fromAmount.multiply(multiplicand).setScale(2, RoundingMode.HALF_UP);
    }
}
