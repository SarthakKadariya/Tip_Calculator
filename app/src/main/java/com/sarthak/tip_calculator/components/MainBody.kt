package com.sarthak.tip_calculator.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarthak.tip_calculator.calculateTotalPerPerson
import com.sarthak.tip_calculator.calculateTotalTip
import com.sarthak.tip_calculator.widgets.RoundIconButton

@Preview
@Composable
fun MainBody() {
    //state hoisting
    val enteredAmountState = remember { mutableStateOf("") }
    val sliderPosition = remember { mutableStateOf(0f) }
    val tipPercentage = (sliderPosition.value * 100).toInt()
    val tipAmountState = remember { mutableStateOf(0.0) }
    val totalPerPerson = remember { mutableStateOf(0.0) }
    val peopleCount = remember { mutableStateOf(1) }
    BillForm(
        tipAmountState = tipAmountState,
        totalPerPerson = totalPerPerson,
        peopleCount = peopleCount,
        enteredAmountState = enteredAmountState,
        sliderPosition = sliderPosition,
        tipPercentage = tipPercentage
    ) {
        totalPerPerson.value = calculateTotalPerPerson(
            it.toDouble(), tipPercentage, peopleCount.value
        )
        tipAmountState.value = calculateTotalTip(
            it.toDouble(), tipPercentage
        )


    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    tipAmountState: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    peopleCount: MutableState<Int>,
    enteredAmountState: MutableState<String>,
    sliderPosition: MutableState<Float>,
    tipPercentage: Int,
    onValChange: (String) -> Unit = {}
) {
    // Stateful variables
    val validState = remember(enteredAmountState.value) {
        enteredAmountState.value.trim().isNotEmpty()

    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = Unit, block = { focusRequester.requestFocus() })

    // Top part for showing actual amount
    TopHeader(totalPerPerson = totalPerPerson.value)

    // Main body for calculating the total amount,everything is inside card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color(0xFFF7F5EB))
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Text field to enter the amount
            InputField(
                modifier = Modifier.focusRequester(focusRequester),
                valueState = enteredAmountState,
                labelId = "Enter Amount",
                enable = true,
                isSingleLine = true,
                keyboardActions = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(enteredAmountState.value.trim())
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },

                )

            // If text field is not empty these components will appear otherwise it will be hidden
            if (validState) {

                // Row containing buttons to split the bill
                Row(
                    modifier = modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = "Split", modifier = Modifier.padding(start = 10.dp))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundIconButton(imageVector = Icons.Filled.Remove, onClick = {
                            if (peopleCount.value > 1) peopleCount.value--
                            totalPerPerson.value = calculateTotalPerPerson(
                                enteredAmountState.value.toDouble(),
                                tipPercentage,
                                peopleCount.value
                            )

                        })

                        Text(text = peopleCount.value.toString())

                        RoundIconButton(imageVector = Icons.Filled.Add, onClick = {
                            peopleCount.value++
                            totalPerPerson.value = calculateTotalPerPerson(
                                enteredAmountState.value.toDouble(),
                                tipPercentage,
                                peopleCount.value
                            )

                        })

                    }


                }
                // Spacer duhh
                Spacer(modifier = Modifier.height(15.dp))

                // Row containing  text that will show tip percentage
                Row(
                    modifier = modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = "Tip", modifier = Modifier.padding(start = 10.dp))
                    Spacer(modifier = Modifier.width(215.dp))
                    Text(text = "${tipAmountState.value}$")

                }
                // Column containing Slider and Text to input amount of tip percentage
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.padding(18.dp)
                ) {
                    Text(text = "$tipPercentage%")
                    Spacer(modifier = Modifier.height(14.dp))
                    Slider(
                        value = sliderPosition.value, onValueChange = { newVal ->
                            sliderPosition.value = newVal
                            tipAmountState.value = calculateTotalTip(
                                enteredAmountState.value.toDouble(), tipPercentage
                            )
                            totalPerPerson.value = calculateTotalPerPerson(
                                enteredAmountState.value.toDouble(),
                                tipPercentage,
                                peopleCount.value
                            )

                        }, steps = 5
                    )

                }

                // if the text field is empty, empty box will be shown
            } else {
                Box() {}
            }

        }

    }
}







