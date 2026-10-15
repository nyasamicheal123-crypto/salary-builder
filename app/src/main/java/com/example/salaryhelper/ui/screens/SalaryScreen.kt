package com.example.salaryhelper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryCalculatorScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }

    var grossSalary by remember { mutableStateOf("5000") }
    var taxDeductions by remember { mutableStateOf("800") }
    
    var rent by remember { mutableStateOf("1200") }
    var groceries by remember { mutableStateOf("400") }
    var food by remember { mutableStateOf("250") }
    var transport by remember { mutableStateOf("300") }
    var bills by remember { mutableStateOf("200") }
    var debt by remember { mutableStateOf("300") }
    var other by remember { mutableStateOf("150") }

    val gross = grossSalary.toDoubleOrNull() ?: 0.0
    val tax = taxDeductions.toDoubleOrNull() ?: 0.0
    val rentVal = rent.toDoubleOrNull() ?: 0.0
    val groceriesVal = groceries.toDoubleOrNull() ?: 0.0
    val foodVal = food.toDoubleOrNull() ?: 0.0
    val transportVal = transport.toDoubleOrNull() ?: 0.0
    val billsVal = bills.toDoubleOrNull() ?: 0.0
    val debtVal = debt.toDoubleOrNull() ?: 0.0
    val otherVal = other.toDoubleOrNull() ?: 0.0

    val netSalary = gross - tax
    val totalExpenses = rentVal + groceriesVal + foodVal + transportVal + billsVal + debtVal + otherVal
    val savings = netSalary - totalExpenses
    val savingsRate = if (netSalary > 0) (savings / netSalary) * 100 else 0.0

    com.example.salaryhelper.ui.theme.SalaryHelperTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("💰 Salary & Budget Helper") },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isDarkTheme) "🌙" else "☀️")
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { isDarkTheme = it }
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("1. Income & Deductions", style = MaterialTheme.typography.titleMedium)
                        InputField("Gross Salary", grossSalary) { grossSalary = it }
                        InputField("Tax & Deductions", taxDeductions) { taxDeductions = it }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("2. Monthly Expenses", style = MaterialTheme.typography.titleMedium)
                        InputField("Rent / Mortgage", rent) { rent = it }
                        InputField("Groceries", groceries) { groceries = it }
                        InputField("Food / Dining Out", food) { food = it }
                        InputField("Transport / Fuel", transport) { transport = it }
                        InputField("Utilities & Bills", bills) { bills = it }
                        InputField("Debt Payments", debt) { debt = it }
                        InputField("Other Expenses", other) { other = it }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("3. Financial Summary", style = MaterialTheme.typography.titleMedium)
                        SummaryRow("Net Salary", "$${String.format("%.2f", netSalary)}")
                        SummaryRow("Total Expenses", "$${String.format("%.2f", totalExpenses)}")
                        SummaryRow("Potential Savings", "$${String.format("%.2f", savings)}")
                        SummaryRow("Savings Rate", "${String.format("%.1f", savingsRate)}%")
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("4. Smart Financial Insights", style = MaterialTheme.typography.titleMedium)
                        
                        if (savings < 0) {
                            Text("🚨 Deficit Alert: Overspending by $${String.format("%.2f", -savings)}!", color = MaterialTheme.colorScheme.error)
                        } else if (savingsRate < 20) {
                            Text("⚠️ Low Savings: Savings rate is under 20%. Consider lowering discretionary costs.")
                        } else {
                            Text("🎉 Great Job! Maintaining a healthy savings rate of ${String.format("%.1f", savingsRate)}%.", color = MaterialTheme.colorScheme.primary)
                        }

                        if (netSalary > 0 && (rentVal / netSalary) > 0.35) {
                            Text("🏠 Housing Warning: Rent takes over 35% of your net income.")
                        }
                        if (netSalary > 0 && ((foodVal + groceriesVal) / netSalary) > 0.25) {
                            Text("🍔 Food Warning: Food & groceries exceed 25% of your net income.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
