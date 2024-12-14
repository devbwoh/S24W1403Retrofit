package kr.ac.kumoh.ce.s20240000.s24w1403retrofit

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage


enum class SongScreen {
    Singer,
    Song,
    SongDetail
}

@Composable
fun SingerList() {
    Box (
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "가수 리스트",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 70.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SongList(
    viewModel: SongViewModel = viewModel(),
    onNavigate: (String) -> Unit,
) {
    val songList by viewModel.songList.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(songList) { song ->
            SongItem(song, onNavigate)
        }
    }
}

@Composable
fun SongDetail(song: Song) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RatingBar(song.rating)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            song.title,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            lineHeight = 45.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = "https://picsum.photos/300/300?random=${song.id}",
            contentDescription = "노래 앨범 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(400.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://i.pravatar.cc/100?u=${song.singer}",
                contentDescription = "가수 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(song.singer, fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))

        song.lyrics?.let {
            Text(
                text = it.replace("\\n","\n"),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                lineHeight = 35.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/results?search_query=노래방+${song.title}")
            )
            context.startActivity(intent)
        }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                YoutubeIcon()
                Spacer(modifier = Modifier.width(16.dp))
                Text("노래방 검색", fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun RatingBar(stars: Int) {
    Row {
        repeat(stars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "stars",
                modifier = Modifier.size(48.dp),
                tint = Color.Red,
            )
        }
    }
}

// 출처
// https://github.com/worstkiller/jetpackcompose_canvas_icon_pack
// https://github.com/worstkiller/jetpackcompose_canvas_icon_pack/blob/master/app/src/main/java/com/vikas/jetpackcomposeiconpack/ShapesActivity.kt
@Composable
fun YoutubeIcon() {
    Canvas(
        modifier = Modifier
            .size(50.dp)
    ) {

        val path = Path().apply {
            moveTo(size.width * .43f, size.height * .38f)
            lineTo(size.width * .72f, size.height * .55f)
            lineTo(size.width * .43f, size.height * .73f)
            close()
        }
        drawRoundRect(
            color = Color.Red,
            cornerRadius = CornerRadius(20f, 20f),
            size = Size(size.width, size.height * .70f),
            topLeft = Offset(size.width.times(.0f), size.height.times(.20f))
        )
        drawPath(color = Color.White, path = path)
    }
}

@Composable
fun SongItem(
    song: Song,
    onNavigate: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .clickable {
                onNavigate(SongScreen.SongDetail.name + "/${song.id}")
            },
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
            AsyncImage(
                model = "https://picsum.photos/300/300?random=${song.singer}",
                contentDescription = "가수 이미지 ${song.singer}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(percent = 10)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TextTitle(song.title)
                TextSinger(song.singer)
            }
        }
    }
}

@Composable
fun TextTitle(title: String) {
    Text(
        title,
        fontSize = 30.sp,
        lineHeight = 35.sp
    )
}

@Composable
fun TextSinger(singer: String) {
    Text(singer, fontSize = 20.sp)
}