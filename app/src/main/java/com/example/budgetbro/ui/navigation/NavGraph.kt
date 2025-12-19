package com.example.budgetbro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.budgetbro.data.auth.AuthRepository
import com.example.budgetbro.data.local.DatabaseProvider
import com.example.budgetbro.data.local.repo.CategoryRepository
import com.example.budgetbro.data.local.repo.TransactionRepository
import com.example.budgetbro.ui.screens.AddTransactionScreen
import com.example.budgetbro.ui.screens.CategoriesScreen
import com.example.budgetbro.ui.screens.CategoryDetailScreen
import com.example.budgetbro.ui.screens.DashboardScreen
import com.example.budgetbro.ui.screens.EditTransactionScreen
import com.example.budgetbro.ui.screens.LoginScreen
import com.example.budgetbro.ui.screens.SignUpScreen
import com.example.budgetbro.ui.screens.TransactionDetailScreen
import com.example.budgetbro.ui.screens.TransactionsScreen
import com.example.budgetbro.ui.viewmodel.AuthViewModel
import com.example.budgetbro.ui.viewmodel.AuthViewModelFactory
import com.example.budgetbro.ui.viewmodel.CategoriesViewModel
import com.example.budgetbro.ui.viewmodel.CategoriesViewModelFactory
import com.example.budgetbro.ui.viewmodel.CategoryDetailViewModel
import com.example.budgetbro.ui.viewmodel.CategoryDetailViewModelFactory
import com.example.budgetbro.ui.viewmodel.DashboardViewModel
import com.example.budgetbro.ui.viewmodel.DashboardViewModelFactory
import com.example.budgetbro.ui.viewmodel.TransactionDetailViewModel
import com.example.budgetbro.ui.viewmodel.TransactionDetailViewModelFactory
import com.example.budgetbro.ui.viewmodel.TransactionsViewModel
import com.example.budgetbro.ui.viewmodel.TransactionsViewModelFactory

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard"
    const val CATEGORIES = "categories"
    const val TRANSACTIONS = "transactions"
    const val ADD_TRANSACTION = "add_transaction"

    const val CATEGORY_DETAIL = "category_detail"
    const val CATEGORY_DETAIL_ROUTE = "category_detail/{categoryId}"
    const val TX_DETAIL = "tx_detail"
    const val TX_EDIT = "tx_edit"
    const val TX_DETAIL_ROUTE = "tx_detail/{txId}"
    const val TX_EDIT_ROUTE = "tx_edit/{txId}"
}

@Composable
fun AppNav() {
    val authVm: AuthViewModel = viewModel(factory = AuthViewModelFactory(AuthRepository()))
    val user by authVm.user.collectAsStateWithLifecycle()

    if (user == null) AuthNav(authVm) else MainNav(authVm)
}

@Composable
private fun AuthNav(authVm: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                authVm = authVm,
                onGoSignUp = { navController.navigate(Routes.SIGNUP) }
            )
        }
        composable(Routes.SIGNUP) {
            SignUpScreen(
                authVm = authVm,
                onGoLogin = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun MainNav(authVm: AuthViewModel) {
    val navController = rememberNavController()
    val user by authVm.user.collectAsStateWithLifecycle()
    val uid = requireNotNull(user).uid

    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {

        composable(Routes.DASHBOARD) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { backStackEntry }

            val context = LocalContext.current
            val db = DatabaseProvider.get(context)
            val txnRepo = TransactionRepository(db.transactionDao())

            val dashVm: DashboardViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = DashboardViewModelFactory(uid, txnRepo)
            )

            DashboardScreen(
                vm = dashVm,
                onGoCategories = { navController.navigate(Routes.CATEGORIES) },
                onGoTransactions = { navController.navigate(Routes.TRANSACTIONS) },
                onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) },
                onOpenTransaction = { txId -> navController.navigate("${Routes.TX_DETAIL}/$txId") },
                onSignOut = { authVm.signOut() }
            )
        }

        composable(Routes.CATEGORIES) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.DASHBOARD)
            }

            val context = LocalContext.current
            val db = DatabaseProvider.get(context)
            val repo = CategoryRepository(db.categoryDao())

            val vm: CategoriesViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = CategoriesViewModelFactory(uid, repo)
            )

            CategoriesScreen(
                vm = vm,
                onBack = { navController.popBackStack() },
                onOpen = { catId -> navController.navigate("${Routes.CATEGORY_DETAIL}/$catId") } // ✅ NEW
            )
        }

        composable(
            route = Routes.CATEGORY_DETAIL_ROUTE,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.DASHBOARD)
            }

            val categoryId = requireNotNull(backStackEntry.arguments?.getString("categoryId"))

            val context = LocalContext.current
            val db = DatabaseProvider.get(context)

            val catRepo = CategoryRepository(db.categoryDao())
            val txRepo = TransactionRepository(db.transactionDao())

            val vm: CategoryDetailViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = CategoryDetailViewModelFactory(uid, categoryId, catRepo, txRepo)
            )

            CategoryDetailScreen(
                vm = vm,
                onBack = { navController.popBackStack() },
                onOpenTransaction = { txId -> navController.navigate("${Routes.TX_DETAIL}/$txId") }
            )
        }

        composable(Routes.TRANSACTIONS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.DASHBOARD)
            }

            val context = LocalContext.current
            val db = DatabaseProvider.get(context)
            val repo = TransactionRepository(db.transactionDao())

            val vm: TransactionsViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TransactionsViewModelFactory(uid, repo)
            )

            TransactionsScreen(
                vm = vm,
                onBack = { navController.popBackStack() },
                onAdd = { navController.navigate(Routes.ADD_TRANSACTION) },
                onOpen = { txId -> navController.navigate("${Routes.TX_DETAIL}/$txId") }
            )
        }

        composable(Routes.ADD_TRANSACTION) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.DASHBOARD)
            }

            val context = LocalContext.current
            val db = DatabaseProvider.get(context)

            val catRepo = CategoryRepository(db.categoryDao())
            val categoriesVm: CategoriesViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = CategoriesViewModelFactory(uid, catRepo)
            )

            val txnRepo = TransactionRepository(db.transactionDao())
            val txVm: TransactionsViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TransactionsViewModelFactory(uid, txnRepo)
            )

            AddTransactionScreen(
                categoriesVm = categoriesVm,
                txVm = txVm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.TX_DETAIL_ROUTE,
            arguments = listOf(navArgument("txId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.DASHBOARD)
            }

            val txId = requireNotNull(backStackEntry.arguments?.getString("txId"))

            val context = LocalContext.current
            val db = DatabaseProvider.get(context)
            val txnRepo = TransactionRepository(db.transactionDao())

            val detailVm: TransactionDetailViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TransactionDetailViewModelFactory(txId, txnRepo)
            )

            val txVm: TransactionsViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TransactionsViewModelFactory(uid, txnRepo)
            )

            TransactionDetailScreen(
                vm = detailVm,
                onBack = { navController.popBackStack() },
                onEdit = { id -> navController.navigate("${Routes.TX_EDIT}/$id") },
                onDelete = { txn -> txVm.delete(txn) }
            )
        }

        composable(
            route = Routes.TX_EDIT_ROUTE,
            arguments = listOf(navArgument("txId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.DASHBOARD)
            }

            val txId = requireNotNull(backStackEntry.arguments?.getString("txId"))

            val context = LocalContext.current
            val db = DatabaseProvider.get(context)

            val catRepo = CategoryRepository(db.categoryDao())
            val categoriesVm: CategoriesViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = CategoriesViewModelFactory(uid, catRepo)
            )

            val txnRepo = TransactionRepository(db.transactionDao())
            val txVm: TransactionsViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TransactionsViewModelFactory(uid, txnRepo)
            )

            val detailVm: TransactionDetailViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TransactionDetailViewModelFactory(txId, txnRepo)
            )

            EditTransactionScreen(
                categoriesVm = categoriesVm,
                txVm = txVm,
                detailVm = detailVm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
