package com.example.dayflow.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing


@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    isError: Boolean,
    errorText: String = stringResource(R.string.this_filed_required),
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    isSingleLine: Boolean = true,
    onClickKeyboardDone: () -> Unit = {},
    onChangeValue: (String) -> Unit,
) {
    Column(modifier = modifier) {
        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = value,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onClickKeyboardDone()
                },
                onNext = { focusManager.moveFocus(FocusDirection.Next) },
                onSearch = {
                    focusManager.clearFocus()
                    onClickKeyboardDone()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = isSingleLine,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
            ),
            textStyle = MaterialTheme.typography.titleMedium,
            shape = RoundedCornerShape(MaterialTheme.spacing.space4),
            onValueChange = onChangeValue
        )
        if (isError)
            Text(
                modifier = Modifier.padding(top = MaterialTheme.spacing.space8),
                text = errorText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
    }
}