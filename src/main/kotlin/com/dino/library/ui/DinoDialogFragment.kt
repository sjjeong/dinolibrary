package com.dino.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dino.library.ext.showToast
import com.dino.library.BR
import org.koin.androidx.viewmodel.ext.android.viewModel


abstract class DinoDialogFragment<B : ViewDataBinding, VM : DinoViewModel>(
    private val layoutId: Int,
    viewModelCls: Class<VM>
) : DialogFragment() {

    protected lateinit var binding: B
        private set

    protected val viewModel by viewModel(clazz = viewModelCls.kotlin)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.vm, viewModel)
        }
        viewModel {
            liveToast.observe(viewLifecycleOwner) { this@DinoDialogFragment.showToast(it) }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().let {
            it.add(this, tag)
            it.commitAllowingStateLoss()
        }
    }

    protected fun binding(action: B.() -> Unit) {
        binding.run(action)
    }

    protected fun viewModel(action: VM.() -> Unit) {
        viewModel.run(action)
    }

}
