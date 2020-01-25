package com.spinevox.app.screens.diagnostic.archive

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spinevox.app.R
import com.spinevox.app.common.showToast
import com.spinevox.app.databinding.LayoutArchiveBinding
import com.spinevox.app.network.InspectionDataResponse
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.network.Utils
import com.spinevox.app.network.serverErrorMessage
import com.spinevox.app.screens.base.LazyFragment
import com.spinevox.app.screens.base.recycler.LazyAdapter
import com.spinevox.app.screens.base.recycler.initWithLinLay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ArchiveFragment : LazyFragment<LayoutArchiveBinding>(), LazyAdapter.OnItemClickListener<ItemArchiveData> {

    override lateinit var binding: LayoutArchiveBinding

    override fun initController(view: View) {
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }

        val listData = mutableListOf<InspectionDataResponse.InspectionDataItem>()

        val token = sharedPreferences.getString("serverToken", "")
        launch {
            try {
                val result = SpineVoxService.getService(context!!).getInspectionList("JWT $token").await()
                listData.addAll(result.data)
                binding.rvContent.initWithLinLay(LinearLayoutManager.VERTICAL, ArchiveAdapter(this@ArchiveFragment), listData.map {
                    //it.diagnosis.
                    val strBuilder = StringBuilder()
                    it.diagnosis?.skoliometry?.let { skoliometry ->
                        Utils.scoliometrData[skoliometry]?.let { value ->
                            strBuilder.append(value)
                        }
                    }

                    it.diagnosis?.twisted_pelvis?.let { pelvis ->
                        Utils.scoliometrData[pelvis]?.let { value ->
                            strBuilder.append(value)
                        }
                    }

                    it.diagnosis?.back?.let { back ->
                        Utils.scoliometrData[back]?.let { value ->
                            strBuilder.append(value)
                        }
                    }

                    it.diagnosis?.leg?.let { leg ->
                        Utils.scoliometrData[leg]?.let { value ->
                            strBuilder.append(value)
                        }
                    }

                    it.diagnosis?.profile?.let { profile ->
                        Utils.scoliometrData[profile]?.let { value ->
                            strBuilder.append(value)
                        }
                    }

                    ItemArchiveData("Сколіометр + Штучній інтелект", it.created_at, strBuilder.toString())
                })
            }  catch (e: HttpException) {
                showToast(context!!, e.serverErrorMessage())
            } catch (e: Throwable) {

            }
        }

//        binding.rvContent.initWithLinLay(LinearLayoutManager.VERTICAL, ArchiveAdapter(this), listOf(
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019")
//        ))
    }

    override fun onLazyItemClick(data: ItemArchiveData) {
        //TODO:::
        val bundle = bundleOf("archiveData" to data)
        findNavController().navigate(R.id.action_archiveFragment_to_archiveInfoFragment, bundle)

//        arguments?.getBundle("amount") as ItemArchiveData
    }

    override fun getLayoutId(): Int = R.layout.layout_archive

}