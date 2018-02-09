
/*
  Created by shiyawei on 17/7/11.
 */

/**
 * 基类Fragment
 */
public abstract class AbsFragment extends Fragment {

    protected View mRootView;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;
    //配合viewpager 首次是否一次性加载对应的fragment
    private boolean loadOnlyOnceImmediately = true;


    public AbsFragment(boolean loadOnlyOnceImmediately) {
        loadOnlyOnceImmediately = loadOnlyOnceImmediately;
    }

    public AbsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState);
            bindView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBindView(mRootView);
        mRootView = null;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            if (loadOnlyOnceImmediately) {
                lazyLoad();
            }
            if (isPrepared) {
                onVisible();
            }
        } else {
            if (isVisible && isPrepared) {
                isVisible = false;
                onInvisible();
            }
        }
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * @desc 懒加载机制
     * @author syw
     * created by 17/10/25 下午2:20
     */
    private void lazyLoad() {
        if (! loadOnlyOnceImmediately) {
            isFirst = false;
            initData();
            return;
        }
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        isFirst = false;
        initData();
    }


    /**
     * @param inflater container savedInstanceState
     * @desc onCreateView()调用 初始化View
     * @author syw
     * created by 17/10/25 下午2:21
     */
    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * @desc onCreateView()中 绑定View
     * @author syw
     * created by 17/10/25 下午1:58
     */
    public abstract void bindView(View view);

    /**
     * @desc 类初始化调用一次 数据懒加载
     * @author syw
     * created by 17/10/25 下午1:58
     */
    public abstract void initData();

    /**
     * @desc 当前 fragment可见
     * @author syw
     * created by 17/10/25 下午2:11
     */
    protected void onVisible() {
    }

    /**
     * @desc 当前 fragment不可见
     * @author syw
     * created by 17/10/25 下午2:00
     */
    protected void onInvisible() {
    }

    /**
     * @desc onDestroyView()中 解绑View
     * @author syw
     * created by 17/10/25 下午1:59
     */
    public abstract void unBindView(View view);

}
