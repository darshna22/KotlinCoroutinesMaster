package th.co.ktb.next.libs.i18n

import android.text.Editable
import android.widget.*
import io.github.inflationx.viewpump.InflateResult
import io.github.inflationx.viewpump.Interceptor

class I18nLayoutInflaterInterceptor : Interceptor {
  @Suppress("MoveVariableDeclarationIntoWHen")
  override fun intercept(chain: Interceptor.Chain): InflateResult {
    val result = chain.proceed(chain.request())
    val view = result.view ?: return result

    when (view) {
      is EditText -> transformEditText(view)
      is RadioButton -> transformRadioButton(view)
      is Button -> transformButton(view)
      is TextView -> transformTextView(view)
      is ImageView -> transformImageView(view)
    }

    return result
  }

  private fun transformTextView(textView: TextView): TextView {
    val text = textView.text.toString()
    if (text.startsWith((PREFIX))) {
      val key = text.removePrefix((PREFIX))
      textView.text = T.get(key)
    }

    val hint = textView.hint?.toString() ?: ""
    if (hint.isNotBlank() && hint.startsWith(PREFIX)) {
      val key = hint.removePrefix(PREFIX)
      textView.hint = T.get(key)
    }

    val contentDescription = textView.contentDescription?.toString() ?: ""
    if (contentDescription.isNotBlank() && contentDescription.startsWith((PREFIX))) {
      val key = contentDescription.removePrefix(PREFIX)
      textView.contentDescription = T.get(key)
    }

    return textView
  }

  private fun transformButton(button: Button): Button {
    val text = button.text.toString()
    if (text.startsWith(PREFIX)) {
      val key = text.removePrefix(PREFIX)
      button.text = T.get(key)
    }
    return button
  }

  private fun transformEditText(editText: EditText): EditText {
    val hint = editText.hint?.toString() ?: ""
    if (hint.isNotBlank() && hint.startsWith(PREFIX)) {
      val key = hint.removePrefix(PREFIX)
      editText.hint = T.get(key)
    }

    val text = editText.text.toString()
    if (text.isNotBlank() && text.startsWith(PREFIX)) {
      val key = text.removePrefix(PREFIX)
      editText.text = Editable.Factory().newEditable(T.get(key))
    }
    return editText
  }

  private fun transformImageView(imageView: ImageView): ImageView {
    val contentDescription = imageView.contentDescription?.toString() ?: ""
    if (contentDescription.isNotBlank() && contentDescription.startsWith((PREFIX))) {
      val key = contentDescription.removePrefix(PREFIX)
      imageView.contentDescription = T.get(key)
    }
    return imageView
  }

  private fun transformRadioButton(radioButton: RadioButton): RadioButton {
    val text = radioButton.text.toString()
    if (text.startsWith(PREFIX)) {
      val key = text.removePrefix(PREFIX)
      radioButton.text = T.get(key)
    }

    val contentDescription = radioButton.contentDescription?.toString() ?: ""
    if (contentDescription.isNotBlank() && contentDescription.startsWith((PREFIX))) {
      val key = contentDescription.removePrefix(PREFIX)
      radioButton.contentDescription = T.get(key)
    }
    return radioButton
  }

  companion object {
    /**
     * [I18nLayoutInflaterInterceptor] will only apply localization if the text
     * in the view starts with this prefix.
     */
    var PREFIX = "@t/"
  }

}
