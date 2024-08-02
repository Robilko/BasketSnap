import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import ru.robilko.core_ui.theme.BasketSnapTheme

@Composable
fun AppOutlinedTextField(
    value: String,
    title: String,
    modifier: Modifier = Modifier,
    maxLength: Int = 256,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    filter: ((String) -> Boolean) = { true },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = BasketSnapTheme.colors.primaryText,
        unfocusedTextColor = BasketSnapTheme.colors.primaryText,
        errorTextColor = BasketSnapTheme.colors.primaryText,
        errorBorderColor = MaterialTheme.colorScheme.error,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedBorderColor = BasketSnapTheme.colors.primaryText,
        unfocusedBorderColor = BasketSnapTheme.colors.secondaryText,
        cursorColor = BasketSnapTheme.colors.primaryText
    ),
    onChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = title },
            value = value,
            onValueChange = {
                val currentValue = it.trimStart()
                if (currentValue.length <= maxLength && filter(currentValue) || currentValue.length < value.length) {
                    onChange(currentValue)
                }
            },
            label = {
                Text(
                    text = title,
                    color = BasketSnapTheme.colors.secondaryText
                )
            },
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions = keyboardActions,
            colors = colors
        )
    }
}