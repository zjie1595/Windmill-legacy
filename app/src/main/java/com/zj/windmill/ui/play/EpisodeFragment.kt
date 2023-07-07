package com.zj.windmill.ui.play

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import com.zj.windmill.R
import com.zj.windmill.databinding.FragmentEpisodeBinding
import com.zj.windmill.model.Episode

private const val ARG_EPISODE_LIST = "episode_list"

class EpisodeFragment : Fragment(R.layout.fragment_episode) {

    private val episodeList = mutableListOf<Episode>()

    private val binding by viewBinding(FragmentEpisodeBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("DEPRECATION")
            it.getParcelableArrayList<Episode>(ARG_EPISODE_LIST)?.apply {
                episodeList.clear()
                episodeList.addAll(this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.divider {
            setDivider(4, true)
            orientation = DividerOrientation.GRID
        }.setup {
            addType<Episode>(R.layout.item_episode)
            onClick(R.id.item_episode) {
                (activity as? PlayActivity)?.onEpisodeClick(getModel())
            }
        }.models = episodeList
    }

    companion object {
        @JvmStatic
        fun newInstance(episodeList: List<Episode>) =
            EpisodeFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_EPISODE_LIST, ArrayList<Episode>().apply {
                        addAll(episodeList)
                    })
                }
            }
    }
}