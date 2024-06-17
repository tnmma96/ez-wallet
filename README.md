# EZ Wallet

EZ Wallet is my attempt to create a simple money manager based on [Ivy Wallet](https://github.com/Ivy-Apps/ivy-wallet) but with Java and XML-based UI.

Click [here](https://www.figma.com/file/tl7m8u09Brhas0Ws4AHVCq/EZ-Wallet) to view app icon design.

Click [here](https://appforshare.io/i/61ekly) to download and install the app.

## Features

- Monitor multiple accounts with balance, total income and total expense
- Create multiple categories to categorize income and expense transactions
- Custom colors for accounts and categories
- 3 transaction types: Income, expense and transfer between 2 accounts
- Sort account list and category list by multiple fields
- Filter transactions by date with detailed information

## Screenshots

|   |   |   |
|:-:|:-:|:-:|
|![Home](images/home_1.jpg)|![Home](images/home_2.jpg)|![Income transaction](images/transaction_income.jpg)|
|![Account](images/account.jpg)|![Account detail](images/account_detail.jpg)|![Expense transaction](images/transaction_expense.jpg)|
|![Category](images/category_income.jpg)|![Category detail](images/category_detail_income.jpg)|![Transfer transaction](images/transaction_transfer.jpg)|

## Technical details

- Java 17 with XML-based UI
- Architecture: MVVM, single-activity
- Room for local data persistence
- RxJava3 for accessing Room database
- ViewModel and LiveData for UI states
- RecyclerView with ListAdapter and DiffUtil for displaying live data list

## To do

### Technical

- Continue to refactor and optimize code
- Create a consistent style for dimension and widget
- Save and restore UI states in case of configuration changes, process death, ...\
  Consider using [Jetpack Navigation](https://developer.android.com/guide/navigation)
- Implement DI with [Dagger](https://developer.android.com/training/dependency-injection/dagger-android) and [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

### Functional

- Light/dark theme switching
- Icon selection
- User screen: User info, settings, ...
- Multiple currencies with exchange rates
- Recurring transactions
- Quick transaction search based on title/description
- Reports
- Export data to CSV
