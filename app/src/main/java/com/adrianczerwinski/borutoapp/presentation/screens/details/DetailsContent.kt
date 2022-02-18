package com.adrianczerwinski.borutoapp.presentation.screens.details

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adrianczerwinski.borutoapp.R
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.adrianczerwinski.borutoapp.domain.model.Hero
import com.adrianczerwinski.borutoapp.presentation.components.InfoBox
import com.adrianczerwinski.borutoapp.presentation.components.OrderedList
import com.adrianczerwinski.borutoapp.ui.theme.*
import com.adrianczerwinski.borutoapp.util.Constants.ABOUT_TEXT_MAX_LINES
import com.adrianczerwinski.borutoapp.util.Constants.BASE_URL
import com.adrianczerwinski.borutoapp.util.Constants.MIN_BACKGROUND_IMAGE

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun DetailsContent(
    navController: NavHostController,
    selectedHero: Hero?
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val currentSheetFraction = scaffoldState.currentSheetFraction
    val radiusAnim by animateDpAsState(
        targetValue = if(currentSheetFraction == 1f) EXTRA_LARGE_PADDING else EXPANDED_RADIUS_LEVEL
    )


    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(
            topStart = radiusAnim,
            topEnd = radiusAnim
        ),
        scaffoldState = scaffoldState,
        sheetPeekHeight = MIN_SHEET_HEIGHT,
        sheetContent ={
            selectedHero?.let { BottomSheetContent(selectedHero = it) }
        },
        content = {

                selectedHero?.let { selectedHero ->
                    BackgroundContent(
                        heroImage = selectedHero.image,
                        imageFraction = currentSheetFraction,
                        onCloseClicked = {
                            navController.popBackStack()
                        })
                }
                }

    )

}

@Composable
fun BottomSheetContent(
    selectedHero: Hero,
    infoBoxIconColor: Color = MaterialTheme.colors.primary,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.titleColor,
) {
    Column(
        modifier = Modifier
            .background(sheetBackgroundColor)
            .padding(all = LARGE_PADDING)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LARGE_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(INFO_ICON_SIZE)
                    .weight(2f),
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = stringResource(id = R.string.app_logo),
                tint = contentColor
            )
            Text(
                modifier = Modifier.weight(8f),
                text = selectedHero.name,
                color = contentColor,
                fontSize = MaterialTheme.typography.h4.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MEDIUM_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(
                icon = painterResource(id = R.drawable.ic_bolt),
                iconColor = infoBoxIconColor,
                bigText = "${selectedHero.power}",
                smallText = stringResource(R.string.power),
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(id = R.drawable.ic_calendar),
                iconColor = infoBoxIconColor,
                bigText = selectedHero.month,
                smallText = stringResource(R.string.month),
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(id = R.drawable.ic_cake),
                iconColor = infoBoxIconColor,
                bigText = selectedHero.day,
                smallText = stringResource(R.string.day),
                textColor = contentColor
            )
        }
        Text(
            text = stringResource(R.string.about),
            color = contentColor,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .padding(bottom = MEDIUM_PADDING),
            text = selectedHero.about,
            color = contentColor,
            fontSize = MaterialTheme.typography.body1.fontSize,
            maxLines = ABOUT_TEXT_MAX_LINES
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OrderedList(
                title = stringResource(R.string.family),
                items = selectedHero.family,
                textColor = contentColor
            )
            OrderedList(
                title = stringResource(R.string.abilities),
                items = selectedHero.abilities,
                textColor = contentColor
            )
            OrderedList(
                title = stringResource(R.string.nature_types),
                items = selectedHero.natureTypes,
                textColor = contentColor
            )

        }

    }

}

@ExperimentalCoilApi
@Composable
fun BackgroundContent(
    heroImage: String,
    imageFraction: Float = 1f,
    backgroundColor: Color = MaterialTheme.colors.surface,
    onCloseClicked: () -> Unit
) {
    val imageUrl = "$BASE_URL${heroImage}"
    val painter = rememberImagePainter(imageUrl){
        error(R.drawable.ic_placeholder)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = imageFraction + MIN_BACKGROUND_IMAGE)
                .align(Alignment.TopCenter),
            painter = painter,
            contentDescription = stringResource(id = R.string.hero_image),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                modifier = Modifier.padding(all = SMALL_PADDING),
                onClick = { onCloseClicked() }) {
                Icon(
                    modifier = Modifier.size(INFO_ICON_SIZE),
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_icon),
                    tint = Color.White
                )
            }
        }
    }

}

@ExperimentalMaterialApi
val BottomSheetScaffoldState.currentSheetFraction: Float
get() {
    val fraction = bottomSheetState.progress.fraction
    val targetValue = bottomSheetState.targetValue
    val currentValue = bottomSheetState.currentValue

    Log.d("Fraction", fraction.toString())
    Log.d("Fraction Target", targetValue.toString())
    Log.d("Fraction Current", currentValue.toString())

    return when {
        currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Collapsed -> 1f
        currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Expanded -> 0f
        currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Expanded -> 1f - fraction
        currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Collapsed -> 0f + fraction
        else -> fraction
    }


}

@Preview
@Composable
fun BottomSheetContentPrev() {
    BottomSheetContent(
        selectedHero = Hero(
        id = 1,
        name = "Naruto",
        image = "",
        about = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        rating = 4.5,
        power = 0,
        month = "Oct",
        day = "1st",
        family = listOf("Minato", "Kushina", "Boruto", "Himawari"),
        abilities = listOf("Sage Mode", "Shadow Clone", "Rasengan"),
        natureTypes = listOf("Earth", "Wind")
    ))
}