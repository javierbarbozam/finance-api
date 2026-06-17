package com.barboza.finance_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.barboza.finance_api.dto.summary.SummaryResponse;
import com.barboza.finance_api.enums.TransactionType;
import com.barboza.finance_api.projection.summary.SummaryProjection;
import com.barboza.finance_api.projection.summary.YearSummaryProjection;

@Service
public class SummaryService {
    private final TransactionService transactionService;
    private final UserService userService;

    SummaryService(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    public SummaryResponse getSummaryByMonth(String email, Integer month, Integer year) {
        var user = userService.findByEmailOrThrow(email);
        var projections = transactionService.getMonthSummary(user, month, year);

        var expense = extractTotal(projections, TransactionType.EXPENSE);
        var income  = extractTotal(projections, TransactionType.INCOME);

        return new SummaryResponse(
            month,
            year,
            income,
            expense,
            income.subtract(expense)
        );
    }

    public List<SummaryResponse> getSummaryYear(String email, Integer year) {
        var user = userService.findByEmailOrThrow(email);

        /*  Example of transactionService.getYearSummary() result:
            [ 
                {month=1, type=INCOME,  total=500},
                {month=1, type=EXPENSE, total=300},
                {month=2, type=EXPENSE, total=150} 
            ]
        */
        var projections = transactionService.getYearSummary(user, year);

        /*  Creates an object using every month as key, the value of every key are the objects above
            In this project there is only 2 possible result for each month because of the type of transaction
            1:[
                {month=1, type=INCOME,  total=500},
                {month=1, type=EXPENSE, total=300},
            ]
         */
        var byMonth = projections.stream()
            .collect(Collectors.groupingBy(YearSummaryProjection::getMonth));

         return IntStream
            .rangeClosed(1, 12) // This forces a 12 month result, even if the projections list doesn't have all months because there is no transactions made
            .mapToObj(month -> {
                var monthProjections = byMonth.getOrDefault(month, List.of()); // Searches for the month in the map, if not present, returns an empty list

                // Obtains the income/expense of every month to return in the response
                var income = extractTotal(monthProjections, TransactionType.INCOME);
                var expense = extractTotal(monthProjections, TransactionType.EXPENSE);
                return new SummaryResponse(month, year, income, expense, income.subtract(expense));
            })
            .toList();

        /*  Expected result:
            [ 
                {month=1, income=500, expense=300, balance=200},
                {month=2, income=0,   expense=150, balance=-150},
                {month=3, income=0,   expense=0,   balance=0},
                ... 12th month
            ]
        */
    }

    // PRIVATE METHODS

    // First parameter allows to use both projections(month and year summary) as they extend the same interface
    private BigDecimal extractTotal(List<? extends SummaryProjection> projections, TransactionType type) {
        return projections.stream()
            .filter(p -> p.getType() == type)
            .map(SummaryProjection::getTotal)
            .findFirst()
            .orElse(BigDecimal.ZERO);
            // Return the income/expense of every month
    }
}
