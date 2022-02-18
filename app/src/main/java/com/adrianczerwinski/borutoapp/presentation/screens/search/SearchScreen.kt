package com.adrianczerwinski.borutoapp.presentation.screens.search

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.adrianczerwinski.borutoapp.presentation.common.ListContent
import com.adrianczerwinski.borutoapp.ui.theme.statusBarColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalCoilApi
@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colors.statusBarColor
    )

    val searchQuery by searchViewModel.searchQuery
    val heroes = searchViewModel.searchedHeroes.collectAsLazyPagingItems()

    Scaffold(topBar = {
        SearchTopBar(
            text = searchQuery,
            onTextChange = {
                searchViewModel.updateSearchQuery(query = it)
            },
            onSearchClicked = {
                              searchViewModel.searchHeroes(query = it)
            },
            onCloseClicked = {
                navController.popBackStack()
//                navController.navigate(route = Screen.SearchScreen.route)
            }
        )
    },
        content = {
            ListContent(heroes = heroes, navController = navController)
        }

    )

}