package com.example.quizapp.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.quizapp.screens.ui.theme.QuizAppTheme
import com.example.quizapp.R
import com.example.quizapp.screens.ui.theme.Purple200
import com.example.quizapp.screens.ui.theme.Purple500
import java.lang.Math.abs
import java.lang.Math.random
import kotlin.random.Random

data class Contributors(val name:String,val role:String,val email:String,val img:Int)
class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        (activity as AppCompatActivity).supportActionBar?.title = "About Us"
        val contributorList= listOf(
            Contributors("Anush Shand","ABC","as@gmail.com", R.drawable.person_placeholder),
            Contributors("A Shand","ABC","as@gmail.com", R.drawable.person_placeholder),
            Contributors("B Shand","DEF","b@gmail.com", R.drawable.person_placeholder),
            Contributors("C Shand","GHI","c@gmail.com", R.drawable.person_placeholder),
            Contributors("D Shand","JKL","d@gmail.com", R.drawable.person_placeholder),
            Contributors("E Shand","MNO","e@gmail.com", R.drawable.person_placeholder),
            Contributors("F Shand","PQR","f@gmail.com", R.drawable.person_placeholder))
            return ComposeView(requireContext()).apply{
                setContent { AboutScreen(contributorList) }
            }
    }
}
@Composable
fun AboutScreen(contributorList:List<Contributors>)
{
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_game),
            contentDescription = "BackgroundImage",
            contentScale = ContentScale.Crop
        )
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    contentScale = ContentScale.FillBounds
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                Text(
                    "Contributors",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(128.dp),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp
                ), content = {
                    items(contributorList.size) { index ->
                        InfoCard(contributorList[index])
                    }
                }
            )
        }
    }
}
@Composable
fun InfoCard(contributor:Contributors,modifier:Modifier=Modifier) {
    val cardFront= remember {
        mutableStateOf(true)
    }
    val rotation = animateFloatAsState(
        targetValue = if(cardFront.value) 0f else 180f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )
    Card(
        modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable { cardFront.value = !cardFront.value }
            .graphicsLayer { rotationY = rotation.value },
        shape= RoundedCornerShape(15.dp),
        elevation = 5.dp
    )
    {
        if(cardFront.value) {
            Box(modifier = Modifier)
            {
                Image(painter = painterResource(id = contributor.img), contentDescription = contributor.name, contentScale = ContentScale.Crop)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomStart
                )
                {
                    Text(contributor.name, style = TextStyle(color = Color.White, fontSize = 16.sp))
                }
            }
        }
        else
        {
            BoxWithConstraints(modifier = Modifier
                .background(Purple200)
                .graphicsLayer { rotationY = 180f })
            {
                val width = constraints.maxWidth
                val height = constraints.maxHeight

                // Medium colored path
                val mediumColoredPoint1 = Offset(0f, height * Random.nextFloat())
                val mediumColoredPoint2 = Offset(width*Random.nextFloat(), height * Random.nextFloat())
                val mediumColoredPoint3 = Offset(width*Random.nextFloat(), height * Random.nextFloat())
                val mediumColoredPoint4 = Offset(width*Random.nextFloat(), height * Random.nextFloat())
                val mediumColoredPoint5 = Offset(width*Random.nextFloat(), -height.toFloat())

                val mediumColoredPath = Path().apply {
                    moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
                    standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
                    standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
                    standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
                    standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
                    lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
                    lineTo(-100f, height.toFloat() + 100f)
                    close()
                }
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    drawPath(
                        path = mediumColoredPath,
                        color = Purple500
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.TopStart
                )
                {
                    Text(contributor.role, style = TextStyle(color = Color.White, fontSize = 16.sp))
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun DefaultPreview() {
    AboutScreen(contributorList = listOf(Contributors("Anush Shand","ABC","as@gmail.com", R.drawable.person_placeholder),
        Contributors("A Shand","ABC","as@gmail.com", R.drawable.person_placeholder),
        Contributors("B Shand","DEF","b@gmail.com", R.drawable.person_placeholder),
        Contributors("C Shand","GHI","c@gmail.com", R.drawable.person_placeholder),
        Contributors("D Shand","JKL","d@gmail.com", R.drawable.person_placeholder),
        Contributors("E Shand","MNO","e@gmail.com", R.drawable.person_placeholder),
        Contributors("F Shand","PQR","f@gmail.com", R.drawable.person_placeholder)))
}

fun Path.standardQuadFromTo(from:Offset,to:Offset)
{
    quadraticBezierTo(from.x, from.y,abs(from.x+to.x)/2f,abs(from.y+to.y)/2f)
}