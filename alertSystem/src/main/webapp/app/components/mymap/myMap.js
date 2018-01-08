(function() {
    'use strict';

    angular
        .module('alertSystemApp').directive("myMap",function(){
            return{
                restrict: 'EA',
                template: '<div id="myMaps"></div>',
                replace: true,
                link: function(scope,element,attrs){
                    var latlang = new google.maps.LatLng(12.944477,77.680285);
                    var myOptions = {
                        zoom:8,
                        center: latlang,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    }
                    new google.maps.Map(element[0],myOptions);
                }
            }


        });
    }

    )
