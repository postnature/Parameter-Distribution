
ParamPeak {

	//peakwidth must be an even number. It denotes the sum of segments on both sides of peak, so 4 means 2 segments on each side of peak, e.g. [ 0.1, 0.4, 1, 0.4, 0.1 ]
	// positive curve value creates a concave envelope, negative vice versa
	* new { | numPoints=12, peakWidth=6, peakPos=0, minVal=0.01, maxVal=1,  curve=3 |

		var envFunc,curves,curvArray,levels,env,durArray,curvelength,array,panenv,outArray,maxi,move;
		curvelength=peakWidth/2;//width of one side of the envelope peak in amount of channels
		durArray=[curvelength,curvelength,(numPoints-1)-(curvelength*2)];//the whole array in 3 legs as long as the amount of channels
		curvArray=[curve,curve.neg,curve.neg];//curves for the env
		levels=[minVal,maxVal,minVal,minVal];//levels

		env=Env(levels,durArray,curvArray);//the actual envelope to generate the array from
		env.duration=numPoints-1;
		array=Array.series(numPoints,0,1);//an array with indexes; one for each channel
		array=env.at(array);// the values of the envelope are read into the index of each channel

		panenv=Env({|i| array.rotate(i)}!numPoints, {1}!(numPoints-1),\lin);//an envelope of envelopes with the value array rotated for each channel, so that every time point in this envelope will generate an array of values going to each channel
		peakPos=peakPos-(peakWidth/2);
		outArray=panenv.at(peakPos%numPoints);//here is the array of now points, this is mapped to each individual synth at a given moment

		^outArray;

	}

}

ParamPeakWarp {

	* new { | numPoints=12, peakPos=1,minVal=0.01,maxVal=1,curve=3|

var envFunc,curves,curvArray,levels,env,segsArray,chArray,outArray;
segsArray=Array.newClear(2);
segsArray.put(0,peakPos);
segsArray.put(1,numPoints-1-peakPos);
curvArray=[curve,curve.neg];
levels=[minVal,maxVal,minVal];
chArray=Array.series(numPoints,0,1);

	env=Env(levels,segsArray,curvArray);
	env.duration=chArray.maxItem;
	outArray=env.at(chArray);
	outArray.postln;

	^outArray;

	}

}

//one dimensional curve with possible duplication using stutter, for stratified latitudes
ParamCurve {

	*new { | numPoints,minVal,maxVal,curve,stutter=nil |

	var env	= Env([minVal,maxVal],[numPoints-1],curve);

	var valarray={ | i |

		env.at(i);

	}!(numPoints);

	if(stutter.notNil && (stutter!=0),{valarray=valarray.stutter(stutter)});

	^valarray;

	}

}

//ParamField.new(numLatitudes:4,numLongitudes:4,frontVal:1000,rearVal:4000,curveLong:4,latTiltF:0.01,latTiltR:-0.2,curveLat:0.1,latCurveWarp:1.1)

//frontVal and rearVal are the extremities of the longitude
//curveLong is the curvature of the envelope along the longitude
//latTiltF, latTiltR are the variation in the amount of lateral tilt across the longitude
//curveLat is the lateral curvature, NB lateral curvature is not if latitude is no more than pairs of speakers.
//latCurveWarp is a ratio of more or less than 1, which gradually warps the curve of latitudes from front to rear (again, no use if lateral dimension is only pairs)

ParamField {

	*new { | numLatitudes, numLongitudes, frontVal, rearVal, curveLong, latTiltF, latTiltR, curveLat, latCurveWarp =1, widthComp=nil |

	var envTiltScale, field;
	var envLong = Env([frontVal,rearVal],[numLatitudes-1], curveLong);
	var valarrayLong={ | i |

		envLong.at(i);

	}!(numLatitudes);

	widthComp.isNil.if({widthComp={1}!numLatitudes});//if widthComp is nil, equal is assumed.
	envTiltScale=Env([latTiltF,latTiltR], [numLatitudes-1], curveLong);

	field=valarrayLong.collect { | val, i |

			var tiltenv=Env([1-(envTiltScale[i]*widthComp[i]), 1/(1-(envTiltScale[i]*widthComp[i]))], [numLongitudes-1], curveLat*(latCurveWarp**i));

			{|i| val*tiltenv[i]}!numLongitudes;

		};

	field=field.flatten;

	^field;

	}

}


///simplified version without lateral envelope for cases where only pairs are used laterally.
//i.e. typical pairwise 8 channel setup
//ParamLatPairs.new(4,2000,6000,8,0.1,-0.1)
//widthcomp should be an array of sam size as numPointsLong, which determines a compensation for the width of loudspeaker pairs.
//E.g. in a 8-channel config 1-2 and 7-8 may be narrower than 3-4 and 5-6, thus the array for widthcomp could be [0.5,1,1,0.5];
ParamLatPairs {

	*new { | numLatitudes, frontVal, rearVal, curveLong, latTiltF, latTiltR, widthComp=nil |

	var envTiltScale, field;
	var envLong = Env([frontVal,rearVal],[numLatitudes-1], curveLong);
	var valarrayLong={ | i |

		envLong.at(i);

	}!(numLatitudes);

	widthComp.isNil.if({widthComp={1}!numLatitudes});//if widthComp is nil, equal is assumed.

	envTiltScale=Env([latTiltF,latTiltR], [numLatitudes-1], curveLong);

	field=valarrayLong.collect { | val, i |

			val*[1-(envTiltScale[i]*widthComp[i]), 1/(1-(envTiltScale[i]*widthComp[i]))];

		};

	field=field.flatten;

	^field;

	}

}

//w=ParamCells(neighbourBiasFunc:nil)
//w.next(2000,0)
//p=ParamCells(warp:2)
//p.next

ParamCells {
	var <>numPoints=8, <>minVal=2000, <>maxVal=12000, <>curve=\exp, <>errorProb=0.1, <>neighbourBiasFunc=nil,<>warp=1;
	var cellArray, cellFunc, routine,outvals, errorfunc, nBias;

	*new { | numPoints=8, minVal=2000, maxVal=12000, curve=\exp, errorProb=0.1, neighbourBiasFunc=nil,warp=1 |

		^super.newCopyArgs(numPoints, minVal, maxVal, curve, errorProb, neighbourBiasFunc,warp).init;

	}

	init {

		switch(curve,
			\exp, {cellArray= Array.exprand(numPoints,minVal,maxVal);
				errorfunc= {exprand(minVal, maxVal)}
			}, //starting data array
			\lin, {cellArray= Array.rand(numPoints,minVal,maxVal);
				errorfunc= {rrand(minVal, maxVal)}
		});

		this.next;//generate an initial value array at instantiation without outputting it
	}

	next { | inVal, index |

		var gravityBias;

		if(inVal.isNil.not && index.isNil.not, {cellArray.put(index, inVal)});

		if(neighbourBiasFunc==nil, {nBias={1.0.rand}},{nBias=neighbourBiasFunc});

		gravityBias=Env([1,warp,1], [1,1]);

		cellArray=cellArray.collect{ | val, i |

			var cell,bias,grav,gravindex;

			bias=nBias.value;
			cell=(bias*cellArray.wrapAt(i-1)) + ((1.0-bias)*cellArray.wrapAt(i+1));

			switch(curve,
			\exp, {gravindex=cell.explin(minVal,maxVal,0,2)},//get the index of the current value in a range 0-2
			\lin, {gravindex=cell.linlin(minVal,maxVal,0,2)}
			);

			grav=gravityBias[gravindex];//determine the pull towards or away from the middle

			//if(gravindex>1, {cell=cell*rrand(1, grav)}, {cell=cell/rrand(1, grav)}); //if gravindex is more than 1 then push cell away from centre

			if(gravindex>1, {
				grav = case
				{grav>1} {cell=cell*rrand(1, grav)}
				{grav<1} {cell=cell/rrand(1, grav)}

			}, {
				grav = case
				{grav>1} {cell=cell/rrand(1, grav)}
				{grav<1} {cell=cell*rrand(1, grav)}
			});

			if(errorProb.coin,{cell=errorfunc.()},cell);//potential error
			cell
			};

		^cellArray;
	}

	//index is both index of inval and index of cell to be updated
	nextCell { | inVal, index |

		var gravityBias,cell,bias,grav,gravindex;

		if(inVal.isNil.not && index.isNil.not, {cellArray.put(index, inVal)});
		if(index.isNil, {index=numPoints.rand});

		gravityBias=Env([1,warp,1], [1,1]);

			bias=nBias.value;
			cell=(bias*cellArray.wrapAt(index-1)) + ((1.0-bias)*cellArray.wrapAt(index+1));

			switch(curve,
			\exp, {gravindex=cell.explin(minVal,maxVal,0,2)}, //get the index of the current value in a range 0-2
			\lin, {gravindex=cell.linlin(minVal,maxVal,0,2)}
			);

			grav=gravityBias[gravindex];//determine the pull towards or away from the middle

		   if(gravindex>1, {cell=cell*rrand(1, grav)}, {cell=cell/rrand(1, grav)}); //if gravindex is more than 1 then push cell away from centre

		if(errorProb.coin,{cell=errorfunc.()},cell);//potential error

		cellArray.put(index, cell);

		^cell
	}

}

ParamCellFunc {

	var <>valArray, >func, <>minVal, <>maxVal, <count=0;

	*new { | valArray, func, minVal, maxVal |

		^super.newCopyArgs(valArray, func, minVal, maxVal);

	}

	next { | inVal, index |
		//one can leave inval and index blank
		if(inVal.isNil.not && index.isNil.not, {valArray.put(index, inVal)});

		valArray=valArray.do{ | val, i |

			valArray.put(i, func.(valArray.wrapAt(i-1),val,valArray.wrapAt(i+1), i));

		};

		valArray=valArray.fold(minVal,maxVal);

		^valArray;

	}

	//iterate just a single cell
	nextCell { | inVal, index, seq=0 |
		//seq 0 means sequence, seq 0 meams rand
		//if inval is nil the value will be whatever is already in the array
		//if index is nil an index will be chosen according to seq

		if(index.isNil, {if(seq==0, {index=count % (valArray.size)}, { index=valArray.size.rand})});

		if(inVal.isNil, {inVal=valArray[index]});

		valArray.put(index, func.(valArray.wrapAt(index-1),inVal,valArray.wrapAt(index+1), index).fold(minVal,maxVal));

		count=count+1;

		^valArray[index];

	}

}

ParamCellsRule {
	//outPut should be \states (for 0s and 1s) \indicesOfOne, or indicesOfZero
	var <>valArray, <>rule, <>errorProb=0, outPut=\states, func;

	*new { | valArray, rule, errorProb=0, outPut=\states |

		^super.newCopyArgs(valArray, rule, errorProb, outPut).init;

	}

	init {

		func = { | leftneighbour, me, rightneighbour, index |

			var array=[leftneighbour,me,rightneighbour];
			var state;
			rule=rule.reverse;

			rule.size.do { | i |

			if(array==(i.asBinaryDigits(3)), {state=rule[i];});

			};

		if (errorProb.coin, { state=[1,0][state];"Error in cell %".postf(index);"".postln;});
		//if error, invert state
		state;

		}

	}

	next {

		var outVals;

		valArray=valArray.do { | val, i |

		valArray.put(i, func.(valArray.wrapAt(i-1),val,valArray.wrapAt(i+1), i));

		};

		switch(outPut,
			\states.asSymbol, { outVals=valArray},
			\indicesOfOne.asSymbol, { outVals=valArray.indicesOfEqual(1)},
			\indicesOfZero.asSymbol, { outVals=valArray.indicesOfEqual(0)}
		)

		^outVals;

	}

}

ParamDeviation {

	* new { | numPoints, val, deviation=0.1, scaleArray=nil, dist=\gauss, boundary= \clip, minVal, maxVal |

	var out;
	var valFunc;

	scaleArray.isNil.if({scaleArray={1}!numPoints});

	valFunc=switch(dist,
		\lin, {{ | i | val*((deviation.rand2+1)*scaleArray[i])}},
		\exp, {{ | i | val*(exprand(1-deviation,1/(1-deviation)))*scaleArray[i]}},
		\gauss, {{ | i | val*(1.gauss(deviation))*scaleArray[i]}},
		\cauchy,{{ | i | val*(1.cauchy(deviation))*scaleArray[i]}},
		\logistic, {{ | i | val*(1.logistic(deviation))*scaleArray[i]}}
	);

	out=valFunc!numPoints;

	out=switch(boundary,
		\clip, {out.clip(minVal,maxVal)},
		\fold, {out.fold(minVal,maxVal)},
		\wrap, {out.wrap(minVal,maxVal)}
	);

	^out;

	}

}

ParamFeed {

	var <>numPoints, <>minVal, <>maxVal, <>inValMul, <>accumFeedMul, <>prevFeedMul, <>deviation, <>array; /*count=0*/

	*new { | numPoints, minVal, maxVal, inValMul=1, accumFeedMul=0.1, prevFeedMul=0.1, deviation=0 |

		^super.newCopyArgs(numPoints, minVal, maxVal, inValMul, accumFeedMul, prevFeedMul, deviation).init;

	}

	init {

		array={0}!numPoints;

	}

	next { | inVal |

		numPoints.do { | i |
			/*feed adjacent channel into this*/
			var newVal = (array.wrapAt((-1..i-1)).sum*accumFeedMul*rrand(1-deviation, 1+deviation))
			/*feed existing (previous) value into this*/
			+ (array[i]*(prevFeedMul*rrand(1-deviation, 1+deviation)))
			/*feed current input value into this*/
			+ (inVal*(inValMul*rrand(1-deviation, 1+deviation)));
		newVal=newVal.fold(minVal,maxVal);
		array.wrapPut(i, newVal);

		};

		^array;
	}

	nextCell { | inVal, index |

		var out;
		var newVal;
		//index.isNil.if({index=count%numPoints});

		newVal= (array.wrapAt((-1..index-1)).sum*(accumFeedMul*rrand(1-deviation, 1+deviation)))
		/*feed adjacent channel into this*/
		+ (array[index]*(prevFeedMul*rrand(1-deviation, 1+deviation)))
		/*feed existing (previous) value into this*/
		+ (inVal*(inValMul*rrand(1-deviation, 1+deviation)));
		/*feed current input value into this*/
		newVal=newVal.fold(minVal,maxVal);
		array.wrapPut(index, newVal);

		//count=index+1;
		^newVal;
	}

}