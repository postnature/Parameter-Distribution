//ex. rotate
//p=PparamPeak(numPoints:8,peakWidth:4,peakPos:Pn(Penv([0,8],[2]),inf),minVal:1,maxVal:6,curve:2,length:inf).asStream
//p.next

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

//rotate
//p=PparamPeakWarp(numPoints:8,peakPos:Pn(Penv([0,8],[2]),inf),minVal:1,maxVal:6,curve:2,length:inf).asStream
//p.next

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

//p=PparamCurve(numPoints:8,minVal:Pexprand(0.5,1.0),maxVal:1.1,curve:2.neg,stutter:nil,length:inf).asStream
//p.next

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

//p=PparamLatPairs(numLatitudes:4,frontVal:1,rearVal:2,curveLong:2,latTiltF:0.2,latTiltR:-0.2,widthComp:nil,length:inf).asStream
//p.next

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


//p=PparamFeed(numPoints:8,inVal:1,minVal:0.5,maxVal:2,inValMul:1.001,accumFeedMul:1.2,prevFeedMul:1.1,deviation:0.1,length:inf).asStream
//p.next

PparamFeed : Pattern {

	var numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation, length;

	*new { | numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation,length |

	^super.newCopyArgs(numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation,length);

	}

	storeArgs { ^[numPoints,inVal,minVal,maxVal,inValMul,accumFeedMul,prevFeedMul,deviation,length ] }

	embedInStream { | inval |

	var paramfeed= ParamFeed(numPoints:numPoints, minVal:minVal,maxVal:maxVal,inValMul:inValMul, accumFeedMul:accumFeedMul,prevFeedMul:prevFeedMul,deviation:deviation);
	var inValStr=inVal.asStream;
	var minValStr=minVal.asStream;
	var maxValStr=maxVal.asStream;
	var inValMulStr=inValMul.asStream;
	var accumFeedMulStr=accumFeedMul.asStream;
	var prevFeedMulStr=prevFeedMul.asStream;
	var deviationStr=deviation.asStream;

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

//p=PparamCellFunc([0.1,0.4,0.2,0.3], {|x,y,z| y=[x,z].choose*rrand(0.9,1.1)},0.3,0.5,inf).asStream
//p.next

PparamCellFunc : Pattern {

	var valArray, func,minVal, maxVal, length=inf;

	*new { arg valArray, func, minVal, maxVal, length=inf;

		^super.newCopyArgs(valArray, func, minVal, maxVal,length);

	}

	storeArgs { ^[valArray, func,minVal, maxVal, length] }

	embedInStream { arg inval;

		var cellArray=ParamCellFunc(valArray, func, minVal, maxVal);
		var valArrayStr=valArray.asStream;
		var funcStr=func.asStream;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;

		length.value(inval).do {

			cellArray.valArray=valArrayStr.next(inval);
			cellArray.func=funcStr.next(inval);
			cellArray.minVal=minValStr.next(inval);
			cellArray.maxVal=maxValStr.next(inval);

			inval=cellArray.next.yield;

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

	*new { arg numPoints=8, inVals,minVal=2000, maxVal=12000, curve=2, errorProb=0.1, neighbourBiasFunc=nil, warp=1, length=inf;

		^super.newCopyArgs(numPoints, inVals, minVal, maxVal, curve, errorProb, neighbourBiasFunc,warp,length);
	}

	storeArgs { ^[numPoints, inVals, minVal, maxVal, curve, errorProb, warp, neighbourBiasFunc,length] }

	embedInStream { arg inval;

		var cellArray=ParamCells(numPoints,minVal, maxVal,curve,errorProb, nil,warp);
		var inValsStr=inVals.asStream;
		var minValStr=minVal.asStream;
		var maxValStr=maxVal.asStream;
		var curveStr=curve.asStream;
		var errorProbStr=errorProb.asStream;
		var neighbourBiasStr=neighbourBiasFunc.asStream;
		var warpStr=warp.asStream;

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
