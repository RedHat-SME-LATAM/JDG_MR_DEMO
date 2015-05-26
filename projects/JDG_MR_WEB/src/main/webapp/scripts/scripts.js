/**
 * 
 */
var app = angular.module('dashboard',  ['angularFileUpload']);
function getGridCount($scope,$http){
	$http.get('rest/billing/CDRGetSize').success(function(resp){
		$scope.dataRecords = resp;
		});
}
function getCDRs($scope,$http,count){
	$http.get('rest/billing/CDRGet?count='+count).success(function(resp){
		$scope.cdrs = resp;
});
}
function getClusterStats($scope,$http){
	$http.get('rest/billing/clusterStats').success(function(resp){
    	$scope.clusterName= resp.clusterName;
    	$scope.nodeCount = resp.clusterMembers.length;
    	$("#nodes").text("");
    	for (i=0;i<resp.clusterMembers.length;i++){
        	var list = $('#nodes');
        	list.simpleGrid({
        		  margin      : 0,
        		  initialSize : 0,
        		  minSize     : 0,
        		});
    		list.append("<li><div class='node'><p align='center'><img src='images/server-alt1_button.png' style='height:40px;'/></p><p align='center'>" + resp.clusterMembers[i] + "</p></div></li>");
        	//$("#nodes").append("<div class='node'><p align='center'><img src='images/server-alt1_button.png' style='height:40px;'/></p><p align='center'>" + resp.clusterMembers[i] + "</p></div>");
            }   	
	});
}
function drawChart(div,data){
	var width = 300,
    height = 250,
    radius = Math.min(width, height) / 2;
	
	var color = d3.scale.ordinal().range(["#3366CC","#DC3912","#FF9900","#109618","#990099"]);
	var arc = d3.svg.arc()
    .outerRadius(radius - 10)
    .innerRadius(0);

var pie = d3.layout.pie()
    .sort(null)
    .value(function(d) { return d.value; });
	
d3.select(div).html('');
var svg = d3.select(div).append("svg")
    .attr("width", width)
    .attr("height", height)
    .append("g")
    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

	  var g = svg.selectAll(".arc")
	      .data(pie(data))
	    .enter().append("g")
	      .attr("class", "arc");

		  g.append("path")
		      .attr("d", arc)
		      .style("fill", function(d) { return color(d.data.key); });
		
		  g.append("text")
		      .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
		      .attr("dy", ".35em")
		      .style("text-anchor", "middle")
		      .text(function(d) { return d.data.key; });
}

app.controller('formCtrl', function($scope,$http,FileUploader) {

	getGridCount($scope,$http);
	getCDRs($scope,$http,20);	
	getClusterStats($scope,$http);	        

	$scope.create= function(cdrFileP){
		$http({method:'POST',url:'rest/billing/CDRUpload',data:{cdrFile:cdrFileP}	,headers: {'Content-Type': 'multipart/form-data'},transformRequest: formDataObject }).success(function(resp){
		});
		};
    
    $scope.execMRBilling = function(){
    	$("#statsChart").html("");
        start = Date.now();
    	$http.get('rest/billing/processBills').success(function(data){
			$scope.keyLabel = 'Line';
        	$scope.bills = data;
        	end = Date.now();
        	$scope.execMRTime = (end-start) + 'ms';
        	});  
    };
    $scope.execMRStats = function(){
        start = Date.now();
    	$http.get('rest/billing/processStats').success(function(data){
    		$scope.keyLabel='Charge Type';
        	$scope.bills = data;
        	end = Date.now();
        	$scope.execMRTime = (end-start) + 'ms';
        	drawChart("#statsChart", data);
        	});
    };

    $("#billBtn").on('click',function(e){
		this.disabled=true;
		$("#busyDiv").addClass("spinner");
         $scope.execMRBilling();
         this.disabled=false;
         $("#busyDiv").removeClass("spinner");
        });

    $("#statsBtn").on('click',function(e){
		this.disabled=true;
		$("#busyDiv").addClass("spinner");
         stats = $scope.execMRStats();
         this.disabled=false;
        $("#busyDiv").removeClass("spinner");
         
        });
    
    $("#refreshGrid").on('click',function(e){ 
        	getGridCount($scope,$http);
        	getCDRs($scope,$http,20);
        	
      });
    $("#refreshNodes").on('click',function(e){ 
    	getClusterStats($scope,$http);	
  		});
	$("#fileUploadModal").on('show.bs.modal',function(){
		document.getElementById('fup').value='';
		$scope.uploader.clearQueue();
		$scope.uploader.progress=0;
		});
	$('#fileUploadModal').on('hide.bs.modal',function(){
		getGridCount($scope,$http);
		getCDRs($scope,$http,20);	
	});

	$scope.uploader= new FileUploader({url:'rest/billing/CDRUpload',alias:'cdrFile'});
	
	$scope.uploader.onSuccessItem= function(fileItem, response, status, headers){$('#fileUploadModal').modal('hide')};
	
});
