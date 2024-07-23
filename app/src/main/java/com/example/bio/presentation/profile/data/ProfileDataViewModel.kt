package com.example.bio.presentation.profile.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.data.api.retrofitProfile
import com.example.bio.data.dto.UpdateBillsDto
import com.example.bio.data.dto.findOneOrder.FindOneOrderUserBillDto
import com.example.bio.domain.entities.Cabinet
import com.example.bio.domain.entities.UpdateBills
import com.example.bio.domain.useCase.GetCabinetFindMyUseCase
import com.example.bio.domain.useCase.PutCabinetUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDataViewModel @Inject constructor(
    private val getCabinetFindMyUseCase: GetCabinetFindMyUseCase,
    private val putCabinetUpdateUseCase: PutCabinetUpdateUseCase
) : ViewModel() {

    private val _cabinetFindMy: MutableSharedFlow<Cabinet> = MutableSharedFlow()
    val cabinetFindMy get() = _cabinetFindMy.asSharedFlow()

    private val _bills: MutableSharedFlow<List<FindOneOrderUserBillDto>> = MutableSharedFlow()
    val bills get() = _bills.asSharedFlow()

    fun getCabinetFindMy(token: String) {
        viewModelScope.launch {
            val res = getCabinetFindMyUseCase.execute(token)
            _cabinetFindMy.emit(res)
        }
    }

    fun putCabinetUpdate(token: String, cabinet: Cabinet) {
        viewModelScope.launch {
            val res = putCabinetUpdateUseCase.execute(token, cabinet)
            _cabinetFindMy.emit(res)
        }
    }

    fun getBills(token: String) {
        viewModelScope.launch {
            val res = retrofitProfile.getBillMy(token)
            _bills.emit(res)
        }
    }

    fun createBills(token: String, updateBills: UpdateBillsDto) {
        viewModelScope.launch {
            val createDeferred = async {
                retrofitProfile.postBillCreate(token, updateBills)
            }
            createDeferred.await() // Ждем завершения создания счета
            getBills(token) // Затем обновляем список счетов
        }
    }

    fun updateBills(token: String, updateBills: UpdateBillsDto, id: Int) {
        viewModelScope.launch {
            val updateDeferred = async {
                retrofitProfile.putBillUpdate(token, updateBills, id)
            }
            updateDeferred.await() // Ждем завершения обновления счета
            getBills(token) // Затем обновляем список счетов
        }
    }

    fun deleteBills(token: String, id: Int) {
        viewModelScope.launch {
            val deleteDeferred = async {
                retrofitProfile.deleteBill(token, id)
            }
            deleteDeferred.await() // Ждем завершения удаления счета
            getBills(token) // Затем обновляем список счетов
        }
    }
}
