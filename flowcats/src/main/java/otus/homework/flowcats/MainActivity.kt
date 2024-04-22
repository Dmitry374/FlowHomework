package otus.homework.flowcats

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel.catsStateFlow
            .filterNotNull()
            .onEach { result ->
                when (result) {
                    is Result.Success<*> -> {
                        val fact = result.value as? Fact
                        if (fact != null) {
                            view.populate(fact)
                        }
                    }

                    is Result.Error -> {
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }.launchIn(lifecycleScope)
    }
}