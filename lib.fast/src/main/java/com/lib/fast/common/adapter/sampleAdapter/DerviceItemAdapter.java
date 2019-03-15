package com.lib.fast.common.adapter.sampleAdapter;

/**
 *        AbsBaseAdapter 的使用示例
 */
//public class DerviceItemAdapter extends AbsBaseAdapter<DerviceListEntity.DataBean,DerviceItemAdapter.MacViewHolder> {
//    private Context context;
//    private List<DerviceListEntity.DataBean> mDatas;
//
//
//
//    IDeviceMakeLinstener iDeviceMakeLinstener ;
//
//    public DerviceItemAdapter(Context context) {
//        super(context, R.layout.dervice_list_item);
//        this.context= context;
//    }
//
//    public void setiDeviceMakeLinstener(IDeviceMakeLinstener iDeviceMakeLinstener) {
//        this.iDeviceMakeLinstener = iDeviceMakeLinstener;
//    }
//
//
//    @Override
//    public MacViewHolder onCreateVH(View itemView, int ViewType) {
//        return new MacViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindDataForItem(MacViewHolder viewHolder, DerviceListEntity.DataBean bean, int position, List<DerviceListEntity.DataBean> mData) {
//
//        viewHolder.name_dervice.setText(bean.getDevicename());
//
//        //查看状态
//        if (bean.getState()==1){
//            viewHolder.state_dervice.setText(context.getResources().getString(R.string.online));
//            viewHolder.state_dervice.setTextColor(Color.parseColor("#3F51B5"));
//        } else if (bean.getState()==2){
//            viewHolder.state_dervice.setText(context.getResources().getString(R.string.noline));
//            viewHolder.state_dervice.setTextColor(Color.parseColor("#CDCDCD"));
//        }
//    }
//
//    @Override
//    protected void setItemListeners(MacViewHolder holder, final DerviceListEntity.DataBean dataBean, final int position) {
//        super.setItemListeners(holder, dataBean, position);
//        holder.lin_dervice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivity(new Intent(context, DerviceDetailsActivity.class).putExtra(DERVICE_NAME,dataBean.getDevicename()).putExtra
//                        (DERVICE_MAC,dataBean.getMac()));
//            }
//        });
//        holder.ReName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                iDeviceMakeLinstener.setReNameListener(position);
//            }
//        });
//        holder.Delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                iDeviceMakeLinstener.setDeleteListener(position);
//
//            }
//        });
//    }
//
//    @Override
//    public DerviceListEntity.DataBean getItemBean(int position) {
//        return super.getItemBean(position);
//    }
//
//    @Override
//    public void setListData(List<DerviceListEntity.DataBean> listData) {
//        super.setListData(listData);
//    }
//
//    public class MacViewHolder extends BaseViewHolder {
//
//        LinearLayout lin_dervice;
//        ImageView im_dervice;
//        TextView   name_dervice;
//        TextView   state_dervice;
//
//        TextView   ReName;
//        TextView   Delete;
//        public MacViewHolder(View itemView) {
//            super(itemView);
//            lin_dervice =  getViewById(R.id.lin_dervice);
//            im_dervice = getViewById(R.id.im_dervice);
//            name_dervice =getViewById(R.id.dervice_name);
//            state_dervice = getViewById(R.id.dervice_state);
//            ReName = getViewById(R.id.right_menu_2);
//            Delete = getViewById(R.id.right_menu_1);
//        }
//    }
//}
//
