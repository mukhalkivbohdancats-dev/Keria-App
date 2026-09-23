package com.example.keriaapp // Вкажи свій package

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// =======================================================
// ⚙️ ТЕКСТИ ТА НАЛАШТУВАННЯ (ЗМІНЮЙ ТУТ)
// =======================================================
private const val MAIN_SCREEN_TITLE = "Система під загрозою!"
private const val ERROR_WINDOW_TITLE = "Критична помилка Windows 98"
private const val ERROR_TEXT = "Сталася неочікувана помилка! Зачиніть це вікно."
private const val TOTAL_TIME_SECONDS = 90 // 1 хвилина 30 секунд (покаже як 1:30)
// =======================================================

class MainActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запуск звуку meow.mp3 з папки res/raw/
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.meow)
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            RetroErrorApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
fun RetroErrorApp() {
    var timeLeft by remember { mutableStateOf(TOTAL_TIME_SECONDS) }
    var showErrorDialog by remember { mutableStateOf(true) }

    // Відлік часу
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    // Формат часу 1:30
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeString = String.format("%d:%02d", minutes, seconds)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF008080) // Бірюзовий фон як у класичному Windows
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = MAIN_SCREEN_TITLE,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Таймер 1:30
            Text(
                text = "Залишилось: $timeString",
                color = Color.Yellow,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (!showErrorDialog) {
                Button(
                    onClick = { showErrorDialog = true },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0C0C0))
                ) {
                    Text("Відкрити помилку знову", color = Color.Black)
                }
            }
        }

        // Вікно помилки
        if (showErrorDialog) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                WindowsRetroDialog(
                    title = ERROR_WINDOW_TITLE,
                    message = ERROR_TEXT,
                    onClose = { showErrorDialog = false }
                )
            }
        }
    }
}

@Composable
fun WindowsRetroDialog(
    title: String,
    message: String,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(310.dp)
            .background(Color(0xFFC0C0C0)) // Сірий фон вікна Windows
            .border(2.dp, Color.White)
            .padding(4.dp)
    ) {
        // Шапка вікна
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF000080)) // Темно-синій заголовок
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            // Хрестик закриття
            Button(
                onClick = onClose,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(18.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0C0C0))
            ) {
                Text("X", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Контент помилки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Іконка помилки
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Red, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = message,
                color = Color.Black,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Кнопка OK
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onClose,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0C0C0)),
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .width(75.dp)
                    .height(32.dp)
            ) {
                Text("OK", color = Color.Black, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}
