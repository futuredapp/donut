package app.futured.donut.sample.cmp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SampleChooserScreen(
                        modifier = Modifier.padding(innerPadding),
                        onCmpSampleClick = {
                            startActivity(Intent(this, CmpSampleActivity::class.java))
                        },
                        onViewsSampleClick = {
                            startActivity(Intent(this, ViewsSampleActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SampleChooserScreen(
    modifier: Modifier = Modifier,
    onCmpSampleClick: () -> Unit,
    onViewsSampleClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.donut_sample),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.choose_implementation),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onCmpSampleClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.compose_multiplatform),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = stringResource(R.string.compose_multiplatform_description),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = onViewsSampleClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.legacy_views),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = stringResource(R.string.legacy_views_description),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
