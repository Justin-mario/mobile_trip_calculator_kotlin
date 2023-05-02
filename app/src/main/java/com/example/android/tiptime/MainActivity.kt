@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.android.tiptime

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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


@Composable
fun EditNumberField(@StringRes label: Int,
                    value: String,
                    onValueChange: (String) -> Unit,
                    keyboardOptions: KeyboardOptions,
                    keyboardActions: KeyboardActions,
                    modifier: Modifier = Modifier) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        label = { Text(text = stringResource(label))
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )



}
@Composable
fun RoundTheTipRow(roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit,
                   modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .size(48.dp),
    verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(checked = roundUp, onCheckedChange = onRoundUpChanged,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End),
        colors = SwitchDefaults.colors(uncheckedThumbColor = Color.Gray))
    }

}
@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double, roundUp: Boolean ): String {
    var tip = tipPercent/100 * amount
    if (roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun TipTimeScreen(modifier: Modifier = Modifier) {
    var amountInput by  remember{ mutableStateOf("") }
    var tipInput  by  remember{ mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }
    
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount = amount, tipPercent, roundUp = roundUp)
    val focus = LocalFocusManager.current


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
        EditNumberField(R.string.bill_amount,amountInput, {amountInput = it},
            keyboardOptions =  KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {focus.clearFocus()}))
        Spacer(modifier = modifier.height(12.dp))
        EditNumberField(R.string.how_was_the_service,tipInput, {tipInput = it},
            keyboardOptions =  KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onNext = {focus.moveFocus(
                FocusDirection.Down)}))
        RoundTheTipRow(roundUp = roundUp, onRoundUpChanged = {roundUp = it} )
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