//(c) 2018 Erik Nyström, published under GNU General Public License, GPLv3.

PparamPeak : Pattern {
	var <>numPoints, <>peakWidth,<>peakPos,<>minVal,<>maxVal,<>curve, <>length;
	*new { arg numPoints, peakWidth,peakPos,minVal,maxVal,curve, length;
		^super.newCopyArgs(numPoints, peakWidth,peakPos,minVal,maxVal,curve, length)
	}
	storeArgs { ^[numPoints, peakWidth,peakPos,minVal,maxVal,curve, length] }
	embedInStream { arg inval;
		var peakWidthStr=peakWidth.asStream;
		var peakPosStr=peakPos.asStream;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;
		var curveStr=curve.asStream;
		length.value(inval).do {
			inval=ParamPeak(numPoints:numPoints,
				peakWidth: peakWidthStr.next(inval),
				peakPos: peakPosStr.next(inval),
				minVal:minValStr.next(inval),
				maxVal:maxValStr.next(inval),
				curve:curveStr.next(inval)
			).yield
		};

		^inval;
	}
}


PparamPeakWarp : Pattern {
	var <>numPoints, <>peakPos,<>minVal,<>maxVal,<>curve, <>length;
	*new { arg numPoints,peakPos,minVal,maxVal,curve, length;
		^super.newCopyArgs(numPoints,peakPos,minVal,maxVal,curve, length)
	}

	storeArgs { ^[numPoints,peakPos,minVal,maxVal,curve, length] }

	embedInStream { arg inval;
		var peakPosStr=peakPos.asStream;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;
		var curveStr=curve.asStream;
		length.value(inval).do {
			inval=ParamPeakWarp(numPoints:numPoints,
				peakPos: peakPosStr.next(inval),
				minVal:minValStr.next(inval),
				maxVal:maxValStr.next(inval),
				curve:curveStr.next(inval)
			).yield
		};

		^inval;
	}
}

PparamCurve : Pattern {
	var <>numPoints, <>minVal, <>maxVal, <>curve, stutter, <>length;
	*new { arg numPoints, minVal, maxVal, curve, stutter=nil, length;
		^super.newCopyArgs(numPoints, minVal, maxVal, curve, stutter, length)
	}

	storeArgs { ^[minVal, maxVal, curve, numPoints, length] }

	embedInStream { arg inval;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;
		var curveStr=curve.asStream;

		length.value(inval).do {
			inval=ParamCurve(numPoints:numPoints,
				minVal:minValStr.next(inval),maxVal:maxValStr.next(inval),
				curve:curveStr.next(inval),stutter:stutter).yield
		};

		^inval;
	}
}

PparamLatPairs : Pattern {
	var <>numLatitudes, <>frontVal, <>rearVal, <>curveLong, <>latTiltF,<>latTiltR,<>widthComp, <>length;

	*new { arg numLatitudes, frontVal, rearVal, curveLong, latTiltF,latTiltR,widthComp,length;

		^super.newCopyArgs(numLatitudes, frontVal, rearVal, curveLong, latTiltF, latTiltR, widthComp, length);

	}

	storeArgs { ^[numLatitudes, frontVal, rearVal, curveLong, latTiltF, latTiltR, widthComp, length] }

	embedInStream { arg inval;

		var frontValStr, rearValStr, curveLongStr, latTiltFStr,latTiltRStr;

		frontValStr = frontVal.asStream;
		rearValStr = rearVal.asStream;
		curveLongStr = curveLong.asStream;
		latTiltFStr = latTiltF.asStream;
		latTiltRStr = latTiltR.asStream;

		length.value(inval).do {
			inval=ParamLatPairs(numLatitudes:numLatitudes,
				frontVal:frontValStr.next(inval),
				rearVal:rearValStr.next(inval),
				curveLong:curveLongStr.next(inval),
				latTiltF:latTiltFStr.next(inval),
				latTiltR:latTiltRStr.next(inval),
				widthComp:widthComp
			).yield
		};

		^inval;
	}
}

PparamField : Pattern {
	var <>numLatitudes, <>numLongitudes, <>frontVal, <>rearVal, <>curveLong, <>latTiltF,<>latTiltR,<>curveLat,<>latCurveWarp,<>widthComp, <>length;

	*new { arg numLatitudes, numLongitudes, frontVal, rearVal, curveLong, latTiltF,latTiltR,curveLat,latCurveWarp,widthComp,length;

		^super.newCopyArgs(numLatitudes, numLongitudes,frontVal, rearVal, curveLong, latTiltF, latTiltR, curveLat,latCurveWarp,widthComp, length);

	}

	storeArgs { ^[numLatitudes, numLongitudes, frontVal, rearVal, curveLong, latTiltF, latTiltR, curveLat, latCurveWarp,widthComp, length] }

	embedInStream { arg inval;

		var frontValStr, rearValStr, curveLongStr, curveLatStr,latTiltFStr,latTiltRStr,latCurveWarpStr;

		frontValStr = frontVal.asStream;
		rearValStr = rearVal.asStream;
		curveLongStr = curveLong.asStream;
		curveLatStr = curveLat.asStream;
		latTiltFStr = latTiltF.asStream;
		latTiltRStr = latTiltR.asStream;
		latCurveWarpStr = latCurveWarp.asStream;

		length.value(inval).do {
			inval=ParamField(numLatitudes:numLatitudes,
				numLongitudes:numLongitudes,
				frontVal:frontValStr.next(inval),
				rearVal:rearValStr.next(inval),
				curveLong:curveLongStr.next(inval),
				latTiltF:latTiltFStr.next(inval),
				latTiltR:latTiltRStr.next(inval),
				curveLat:curveLatStr.next(inval)
				latCurveWarp:latCurveWarpStr.next(inval),
				widthComp:widthComp
			).yield
		};

		^inval;
	}
}

PparamFeed : Pattern {

	var numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation, length;

	*new { | numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation,length |

	^super.newCopyArgs(numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation,length);

	}

	storeArgs { ^[numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation,length ] }

	embedInStream { | inval |

	var inValStr=inVal.asStream;
	var minValStr=minVal.asStream;
	var maxValStr=maxVal.asStream;
	var inValMulStr=inValMul.asStream;
	var accumFeedMulStr=accumFeedMul.asStream;
	var prevFeedMulStr=prevFeedMul.asStream;
	var deviationStr=deviation.asStream;
	var paramfeed= ParamFeed(numPoints:numPoints, minVal:minValStr.next,maxVal:maxValStr.next,inValMul:inValMulStr.next, accumFeedMul:accumFeedMulStr.next,prevFeedMul:prevFeedMulStr.next,deviation:deviationStr.next);

		length.value(inval).do {

			paramfeed.minVal=minValStr.next(inval);
			paramfeed.maxVal=maxValStr.next(inval);
			paramfeed.inValMul=inValMulStr.next(inval);
			paramfeed.accumFeedMul=accumFeedMulStr.next(inval);
			paramfeed.prevFeedMul=prevFeedMulStr.next(inval);
			paramfeed.deviation=deviationStr.next(inval);

			inval=paramfeed.next(inValStr.next(inval)).yield;

		};

		^inval;

	}

}


PparamCellFunc : Pattern {

	var valArray, inVal,func,minVal, maxVal, length=inf;

	*new { arg valArray, inVal,func, minVal, maxVal, length=inf;

		^super.newCopyArgs(valArray, inVal,func, minVal, maxVal,length);

	}

	storeArgs { ^[valArray, inVal, func,minVal, maxVal, length] }

	embedInStream { arg inval;

		var valArrayStr=valArray.asStream;
		var funcStr=func.asStream;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;
		var invalStr=inVal.asStream;
		var cellArray=ParamCellFunc(valArrayStr.next, funcStr.next, minValStr.next, maxValStr.next);
		length.value(inval).do {

			cellArray.valArray=valArrayStr.next(inval);
			cellArray.func=funcStr.next(inval);
			cellArray.minVal=minValStr.next(inval);
			cellArray.maxVal=maxValStr.next(inval);

			inval=cellArray.next(invalStr.next(inval)).yield;

		};

		^inval;

	}

}

PparamDeviation : Pattern {

	var numPoints=8, val, deviation=0.1, scaleArray=nil, dist=\gauss, boundary= \clip, minVal, maxVal, length=inf;

	*new { arg numPoints=8, val, deviation=0.1, scaleArray=nil, dist=\gauss, boundary= \clip, minVal, maxVal, length=inf;

		^super.newCopyArgs(numPoints, val, deviation, scaleArray, dist, boundary, minVal, maxVal, length);

	}

	storeArgs { ^[numPoints, val, deviation, scaleArray, dist, boundary, minVal, maxVal, length] }

	embedInStream { arg inval;

		var valStr=val.asStream;
		var devStr=deviation.asStream;
		var scaleArrayStr=scaleArray.asStream;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;

	length.value(inval).do {

		inval=ParamDeviation(numPoints,
				valStr.next(inval),
				devStr.next(inval),
				scaleArrayStr.next(inval),
				dist, boundary,
				minValStr.next(inval),
				maxValStr.next(inval));
		inval.yield

		};

		^inval;
	}
}


//p=PparamDeviation(8,val: 1,deviation:Pexprand(0.1,0.2),scaleArray:PparamCurve(8,1,2,2,0,inf),minVal:0.1,maxVal:4, length:inf).asStream
//p.next

//p=PparamCells(numPoints:8,inVals:Array.rand(8,0.1,0.9),minVal:0.1,maxVal:1,curve:\exp,errorProb:0.1,neighbourBiasFunc:{1.0.rand},warp:1,length:inf).asStream
//p.next

PparamCells : Pattern {

	var numPoints=8, inVals,minVal=2000, maxVal=12000, curve=\exp, errorProb=0.1, neighbourBiasFunc=nil,warp=1, length=inf;

	*new { arg numPoints=8, inVals,minVal=2000, maxVal=12000, curve=\exp, errorProb=0.1, neighbourBiasFunc=nil, warp=1, length=inf;

		^super.newCopyArgs(numPoints, inVals, minVal, maxVal, curve, errorProb, neighbourBiasFunc,warp,length);
	}

	storeArgs { ^[numPoints, inVals, minVal, maxVal, curve, errorProb, warp, neighbourBiasFunc,length] }

	embedInStream { arg inval;

		var inValsStr=inVals.asStream;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;
		var curveStr=curve.asStream;
		var errorProbStr=errorProb.asStream;
		var neighbourBiasStr=neighbourBiasFunc.asStream;
		var warpStr=warp.asStream;

		var cellArray=ParamCells(numPoints,minValStr.next, maxValStr.next,curveStr.next,errorProbStr.next,neighbourBiasStr.next,warpStr.next);

		length.value(inval).do {

			cellArray.minVal=minValStr.next(inval);
			cellArray.maxVal=maxValStr.next(inval);
			cellArray.curve=curveStr.next(inval);
			cellArray.errorProb= errorProbStr.next(inval);
			cellArray.neighbourBiasFunc=neighbourBiasStr.next(inval);
			cellArray.warp=warpStr.next(inval);

			inval=cellArray.next.yield

		};

		^inval;

	}
}
