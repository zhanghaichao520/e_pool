/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
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
import com.baidu.mapapi.search.route.BikingRouteLine;

import java.util.ArrayList;
import java.util.List;

/**
 * 鐢ㄤ簬鏄剧ず楠戣璺嚎鐨凮verlay
 */
public class BikingRouteOverlay extends OverlayManager {

    private BikingRouteLine mRouteLine = null;

    public BikingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    /**
     * 璁剧疆璺嚎鏁版嵁銆�     *
     * @param line
     *            璺嚎鏁版嵁
     */
    public void setData(BikingRouteLine line) {
        mRouteLine = line;
    }

    @Override
    public final List<OverlayOptions> getOverlayOptions() {
        if (mRouteLine == null) {
            return null;
        }

        List<OverlayOptions> overlayList = new ArrayList<OverlayOptions>();
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
            for (BikingRouteLine.BikingStep step : mRouteLine.getAllStep()) {
                Bundle b = new Bundle();
                b.putInt("index", mRouteLine.getAllStep().indexOf(step));
                if (step.getEntrance() != null) {
                    overlayList.add((new MarkerOptions())
                            .position(step.getEntrance().getLocation())
                                    .rotate((360 - step.getDirection()))
                                            .zIndex(10)
                                                    .anchor(0.5f, 0.5f)
                                                            .extraInfo(b)
                                                                    .icon(BitmapDescriptorFactory
                                                                            .fromAssetWithDpi("Icon_line_node.png")));
                }

                // 鏈�悗璺缁樺埗鍑哄彛鐐�                
                if (mRouteLine.getAllStep().indexOf(step) == (mRouteLine
                        .getAllStep().size() - 1) && step.getExit() != null) {
                    overlayList.add((new MarkerOptions())
                            .position(step.getExit().getLocation())
                                    .anchor(0.5f, 0.5f)
                                            .zIndex(10)
                                                    .icon(BitmapDescriptorFactory
                                                            .fromAssetWithDpi("Icon_line_node.png")));

                }
            }
        }
        // starting
        if (mRouteLine.getStarting() != null) {
            overlayList.add((new MarkerOptions())
                    .position(mRouteLine.getStarting().getLocation())
                            .icon(getStartMarker() != null ? getStartMarker() :
                                    BitmapDescriptorFactory
                                            .fromAssetWithDpi("Icon_start.png")).zIndex(10));
        }
        // terminal
        if (mRouteLine.getTerminal() != null) {
            overlayList
                    .add((new MarkerOptions())
                            .position(mRouteLine.getTerminal().getLocation())
                                    .icon(getTerminalMarker() != null ? getTerminalMarker() :
                                            BitmapDescriptorFactory
                                                    .fromAssetWithDpi("Icon_end.png"))
                                                            .zIndex(10));
        }

        // poly line list
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
            LatLng lastStepLastPoint = null;
            for (BikingRouteLine.BikingStep step : mRouteLine.getAllStep()) {
                List<LatLng> watPoints = step.getWayPoints();
                if (watPoints != null) {
                    List<LatLng> points = new ArrayList<LatLng>();
                    if (lastStepLastPoint != null) {
                        points.add(lastStepLastPoint);
                    }
                    points.addAll(watPoints);
                    overlayList.add(new PolylineOptions().points(points).width(10)
                            .color(getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255)).zIndex(0));
                    lastStepLastPoint = watPoints.get(watPoints.size() - 1);
                }
            }

        }

        return overlayList;
    }

    /**
     * 瑕嗗啓姝ゆ柟娉曚互鏀瑰彉榛樿璧风偣鍥炬爣
     *
     * @return 璧风偣鍥炬爣
     */
    public BitmapDescriptor getStartMarker() {
        return null;
    }
    public int getLineColor() {
        return 0;
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
     * 澶勭悊鐐瑰嚮浜嬩欢
     *
     * @param i
     *            琚偣鍑荤殑step鍦�     *            {@link com.baidu.mapapi.search.route.BikingRouteLine#getAllStep()}
     *            涓殑绱㈠紩
     * @return 鏄惁澶勭悊浜嗚鐐瑰嚮浜嬩欢
     */
    public boolean onRouteNodeClick(int i) {
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().get(i) != null) {
            Log.i("baidumapsdk", "BikingRouteOverlay onRouteNodeClick");
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
        // TODO Auto-generated method stub
        return false;
    }
}