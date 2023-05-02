package com.example.android.tiptime

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(@StringRes label: Int,
                    value: String,
                    onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier){

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        label = { Text(text = stringResource(label))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )



}

@Composable
private fun calculateTip(amount: Double, tipPercent: Double ): String{
    val tip = tipPercent/100 * amount

    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun TipTimeScreen(modifier: Modifier = Modifier) {
    var amountInput by  remember{ mutableStateOf("") }
    var tipInput  by  remember{ mutableStateOf("") }
    
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    val tip = calculateTip(amount = amount, tipPercent)


    Column(modifier = Modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            fontSize = 24.sp,
            text = stringResource(R.string.calculate_tip),
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(R.string.bill_amount,amountInput, {amountInput = it})
        Spacer(modifier = modifier.height(12.dp))
        EditNumberField(R.string.how_was_the_service,tipInput, {tipInput = it})
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(id = R.string.tip_amount, tip),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}