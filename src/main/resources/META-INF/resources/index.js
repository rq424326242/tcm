/**
 * 1、叠加图层
 * 2、鼠标移动获取坐标
 * 3、鼠标移动跟随增加小窗口
 * 4、点击后跳转到实景
 */

var layers = [];
var map;
var geoUrl = "http://localhost:8080/geoserver/dss/wms";
var overlay;

/**
 * OpenLayers调用geoServer发布的wms地图服务
 */
function init(){
	
	//定义提示框的html
	var container = document.getElementById('popup');
	var content = document.getElementById('popup-content');
	//定义地图覆盖物
	overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */ ({
		element: container,
		autoPan: true,
		autoPanAnimation: {
			duration: 250
		}
	}));
	
	
	layers = [
//	              官方地图，在线的
//      new ol.layer.Tile({
//    	  source: new ol.source.OSM()
//      }),
      new ol.layer.Tile({
    	  extent: [8175445.558800001, 2056264.7553000003, 1.50378082604E7, 7087593.985299997],
    	  source: new ol.source.TileWMS({
    		  url: geoUrl,
    		  params: {'LAYERS': 'dss:sheng', 'TILED': true},
    		  serverType: 'geoserver'
    	  })
      }),
      new ol.layer.Tile({
    	  extent: [8369887.4069,2072338.3709000014,1.4813478870099997E7,6472302.065299998],
    	  source: new ol.source.TileWMS({
    		  url: geoUrl,
    		  params: {'LAYERS': 'dss:gaosu', 'TILED': true},
    		  serverType: 'geoserver'
    	  })
      })
	];
	map = new ol.Map({
		layers: layers,
		overlays: [overlay],//加载地图时，将覆盖物添加到地图的覆盖层上
		target: 'map',
		view: new ol.View({
			center: [12040319, 4353865],
			zoom: 5
		})
	});
	
	
	//鼠标移动，获取坐标
	map.on('pointermove', function(evt){
		//web墨卡托坐标
//		console.log(evt.coordinate[0]+"--"+evt.coordinate[1]);
		
		//转经纬度坐标
		//第一个参数：源坐标系；第二个参数：目标坐标系
//		console.log(ol.proj.transform(evt.coordinate,'EPSG:3857', 'EPSG:4326'));
		
		
		//鼠标跟随的提示窗，显示当前鼠标的坐标
		content.innerHTML = '<p>You here:</p><code>' + ol.proj.transform(evt.coordinate,'EPSG:3857', 'EPSG:4326') + '</code>';
		overlay.setPosition(evt.coordinate);
		
	});
	
	
	
	//定义地图移动结束后的事件
//	map.on('moveend', onMoveEnd);
}

//地图移动后触发，获取左上右下的边界坐标，可以是原始的，也可以是经纬度的
function onMoveEnd(evt) {
	var map = evt.map;
	var extent = map.getView().calculateExtent(map.getSize());
	var bottomLeft = ol.proj.transform(ol.extent.getBottomLeft(extent),
			'EPSG:3857', 'EPSG:4326');
	var topRight = ol.proj.transform(ol.extent.getTopRight(extent),
			'EPSG:3857', 'EPSG:4326');
	console.log(bottomLeft+"=="+topRight);
}

