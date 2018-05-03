package com.baidu.mapapi.overlayutil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;

import java.util.ArrayList;
import java.util.List;

/**
 * 鐢ㄤ簬鏄剧ず涓�潯椹捐溅璺嚎鐨刼verlay锛岃嚜3.4.0鐗堟湰璧峰彲瀹炰緥鍖栧涓坊鍔犲湪鍦板浘涓樉绀猴紝褰撴暟鎹腑鍖呭惈璺喌鏁版嵁鏃讹紝鍒欓粯璁や娇鐢ㄨ矾鍐电汗鐞嗗垎娈电粯鍒� */
public class DrivingRouteOverlay extends OverlayManager {

    private DrivingRouteLine mRouteLine = null;
    boolean focus = false;

    /**
     * 鏋勯�鍑芥暟
     * 
     * @param baiduMap
     *            璇rivingRouteOvelray寮曠敤鐨�BaiduMap
     */
    public DrivingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public final List<OverlayOptions> getOverlayOptions() {
        if (mRouteLine == null) {
            return null;
        }

        List<OverlayOptions> overlayOptionses = new ArrayList<OverlayOptions>();
        // step node
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
            
            for (DrivingRouteLine.DrivingStep step : mRouteLine.getAllStep()) {
                Bundle b = new Bundle();
                b.putInt("index", mRouteLine.getAllStep().indexOf(step));
                if (step.getEntrance() != null) {
                    overlayOptionses.add((new MarkerOptions())
                            .position(step.getEntrance().getLocation())
                                    .anchor(0.5f, 0.5f)
                                            .zIndex(10)
                                                    .rotate((360 - step.getDirection()))
                                                            .extraInfo(b)
                                                                    .icon(BitmapDescriptorFactory
                                                                            .fromAssetWithDpi("Icon_line_node.png")));
                }
                // 鏈�悗璺缁樺埗鍑哄彛鐐�               
                if (mRouteLine.getAllStep().indexOf(step) == (mRouteLine
                        .getAllStep().size() - 1) && step.getExit() != null) {
                    overlayOptionses.add((new MarkerOptions())
                            .position(step.getExit().getLocation())
                                    .anchor(0.5f, 0.5f)
                                            .zIndex(10)
                                                    .icon(BitmapDescriptorFactory
                                                            .fromAssetWithDpi("Icon_line_node.png")));

                }
            }
        }

        if (mRouteLine.getStarting() != null) {
            overlayOptionses.add((new MarkerOptions())
                    .position(mRouteLine.getStarting().getLocation())
                            .icon(getStartMarker() != null ? getStartMarker() :
                                    BitmapDescriptorFactory
                                            .fromAssetWithDpi("Icon_start.png")).zIndex(10));
        }
        if (mRouteLine.getTerminal() != null) {
            overlayOptionses
                    .add((new MarkerOptions())
                            .position(mRouteLine.getTerminal().getLocation())
                                    .icon(getTerminalMarker() != null ? getTerminalMarker() :
                                            BitmapDescriptorFactory
                                                    .fromAssetWithDpi("Icon_end.png"))
                                                            .zIndex(10));
        }
        // poly line
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
        
            List<DrivingStep> steps = mRouteLine.getAllStep();
            int stepNum = steps.size();
            
            
            List<LatLng> points = new ArrayList<LatLng>();
            ArrayList<Integer> traffics = new ArrayList<Integer>();
            int totalTraffic = 0;
            for (int i = 0; i < stepNum ; i++) {
                if (i == stepNum - 1) {
                    points.addAll(steps.get(i).getWayPoints());
                } else {
                    points.addAll(steps.get(i).getWayPoints().subList(0, steps.get(i).getWayPoints().size() - 1));
                }
                
                totalTraffic += steps.get(i).getWayPoints().size() - 1;
                if (steps.get(i).getTrafficList() != null && steps.get(i).getTrafficList().length > 0) {
                    for (int j = 0;j < steps.get(i).getTrafficList().length;j++) {
                        traffics.add(steps.get(i).getTrafficList()[j]);
                    }
                }
            }
            
//            Bundle indexList = new Bundle();
//            if (traffics.size() > 0) {
//                int raffic[] = new int[traffics.size()];
//                int index = 0;
//                for (Integer tempTraff : traffics) {
//                    raffic[index] = tempTraff.intValue();
//                    index++;
//                }
//                indexList.putIntArray("indexs", raffic);
//            }
            boolean isDotLine = false;
            
            if (traffics != null && traffics.size() > 0) {
                isDotLine = true;
            }
            PolylineOptions option = new PolylineOptions().points(points).textureIndex(traffics)
                    .width(7).dottedLine(isDotLine).focus(true)
                        .color(getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255)).zIndex(0);
            if (isDotLine) {
                option.customTextureList(getCustomTextureList());
            }
            overlayOptionses.add(option);
        }
        return overlayOptionses;
    }

    /**
     * 璁剧疆璺嚎鏁版嵁
     * 
     * @param routeLine
     *            璺嚎鏁版嵁
     */
    public void setData(DrivingRouteLine routeLine) {
        this.mRouteLine = routeLine;
    }

    /**
     * 瑕嗗啓姝ゆ柟娉曚互鏀瑰彉榛樿璧风偣鍥炬爣
     * 
     * @return 璧风偣鍥炬爣
     */
    public BitmapDescriptor getStartMarker() {
        return null;
    }

    /**
     * 瑕嗗啓姝ゆ柟娉曚互鏀瑰彉榛樿缁樺埗棰滆壊
     * @return 绾块鑹�     */
    public int getLineColor() {
        return 0;
    }
    public List<BitmapDescriptor> getCustomTextureList() {
        ArrayList<BitmapDescriptor> list = new ArrayList<BitmapDescriptor>();
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_blue_arrow.png"));
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_green_arrow.png"));
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_yellow_arrow.png"));
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_red_arrow.png"));
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_nofocus.png"));
        return list;
    }
    /**
     * 瑕嗗啓姝ゆ柟娉曚互鏀瑰彉榛樿缁堢偣鍥炬爣
     * 
     * @return 缁堢偣鍥炬爣
     */
    public BitmapDescriptor getTerminalMarker() {
        return null;
    }

    /**
     * 瑕嗗啓姝ゆ柟娉曚互鏀瑰彉榛樿鐐瑰嚮澶勭悊
     * 
     * @param i
     *            绾胯矾鑺傜偣鐨�index
     * @return 鏄惁澶勭悊浜嗚鐐瑰嚮浜嬩欢
     */
    public boolean onRouteNodeClick(int i) {
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().get(i) != null) {
            Log.i("baidumapsdk", "DrivingRouteOverlay onRouteNodeClick");
        }
        return false;
    }

    @Override
    public final boolean onMarkerClick(Marker marker) {
        for (Overlay mMarker : mOverlayList) {
            if (mMarker instanceof Marker && mMarker.equals(marker)) {
                if (marker.getExtraInfo() != null) {
                    onRouteNodeClick(marker.getExtraInfo().getInt("index"));
                }
            }
        }
        return true;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        boolean flag = false;
        for (Overlay mPolyline : mOverlayList) {
            if (mPolyline instanceof Polyline && mPolyline.equals(polyline)) {
                // 閫変腑
                flag = true;
                break;
            }
        }
        setFocus(flag);
        return true;
    }

    public void setFocus(boolean flag) {
        focus = flag;
        for (Overlay mPolyline : mOverlayList) {
            if (mPolyline instanceof Polyline) {
                // 閫変腑
                ((Polyline) mPolyline).setFocus(flag);

                break;
            }
        }

    }
}
