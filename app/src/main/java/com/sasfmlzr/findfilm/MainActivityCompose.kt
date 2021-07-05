package com.sasfmlzr.findfilm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColor
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivityCompose : AppCompatActivity() {

    @Preview
    @Composable
    fun JetnewsApp(
        //    appContainer: AppContainer
    ) {
        JetnewsTheme {
            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()
                /*                  SideEffect {
                                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
                            }
            */
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                // This top level scaffold contains the app drawer, which needs to be accessible
                // from multiple screens. An event to open the drawer is passed down to each
                // screen that needs it.
                val scaffoldState = rememberScaffoldState()

                val bottomNavigationItems = listOf(
                    BottomNavigationScreens.First,
                    BottomNavigationScreens.Second,
                    BottomNavigationScreens.Third
                )
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute =
                    navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
                Scaffold(
                    scaffoldState = scaffoldState,
                    bottomBar = {
                        SpookyAppBottomNavigation(navController, bottomNavigationItems)
                    }
                ) {
                    JetnewsNavGraph(
                        //   appContainer = appContainer,
                        navController = navController,
                        scaffoldState = scaffoldState
                    )
                }
            }
        }
    }

    @Composable
    private fun SpookyAppBottomNavigation(
        navController: NavHostController,
        items: List<BottomNavigationScreens>
    ) {
        BottomNavigation(backgroundColor = colorLightGray) {
            //  val currentRoute = currentRoute(navController)
            val defaultPos = BottomNavigationScreens.First.route
            val pos = remember { mutableStateOf(defaultPos) }
            val context = LocalContext.current

            items.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(stringResource(id = screen.resourceId)) },
                    selected = pos.value == screen.route,
                    onClick = {
                        if (screen.route != pos.value) {
                            pos.value = screen.route
                        }
                        Toast.makeText(context, screen.resourceId, Toast.LENGTH_SHORT).show()
                    },
                    unselectedContentColor = Color.DarkGray,
                    selectedContentColor = colorPrimary
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            JetnewsApp()
        }
    }
}