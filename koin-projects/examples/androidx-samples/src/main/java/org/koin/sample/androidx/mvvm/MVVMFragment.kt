package org.koin.sample.androidx.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.junit.Assert.*
import org.koin.android.ext.android.getKoin
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateSharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.sample.android.R
import org.koin.sample.androidx.components.ID
import org.koin.sample.androidx.components.mvvm.SavedStateViewModel
import org.koin.sample.androidx.components.mvvm.SimpleViewModel
import org.koin.sample.androidx.components.scope.Session

class MVVMFragment(val session: Session) : Fragment() {

    val shared: SimpleViewModel by sharedViewModel { parametersOf(ID) }
    val simpleViewModel: SimpleViewModel by viewModel { parametersOf(ID) }

    val saved by stateViewModel<SavedStateViewModel> { parametersOf(ID) }
    val sharedSaved: SavedStateViewModel by sharedViewModel { parametersOf(ID) }
    val sharedSaved2 by stateSharedViewModel<SavedStateViewModel> { parametersOf(ID) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mvvm_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assertNotNull(session)

        assertNotEquals(shared, simpleViewModel)
        assertNotEquals((requireActivity() as MVVMActivity).savedVm, saved)

        assertEquals((requireActivity() as MVVMActivity).simpleViewModel, shared)
        assertEquals((requireActivity() as MVVMActivity).savedVm, sharedSaved)
        assertEquals((requireActivity() as MVVMActivity).savedVm, sharedSaved)
        assertEquals((requireActivity() as MVVMActivity).savedVm, sharedSaved)
        assertEquals((requireActivity() as MVVMActivity).savedVm, sharedSaved2)
        assertEquals(sharedSaved, sharedSaved2)

        assertEquals(requireActivity().lifecycleScope.get<Session>().id, getKoin().getProperty("session_id"))
    }
}
