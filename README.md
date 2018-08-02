# Parameter Distribution

The Parameter Distribution library features a set of tools for SuperCollider. They allow for generating structured arrays of values which can be set to parameters of spatially distributed synthesis processes, in order to synthesise a spatially differentiated texture. The parameter distribution classes can be used for anything that requires an array of values, but are thought out for application in multichannel music; it is recommended that they be used for 8 more channels. The envelope based processes tend to generate better results with large quantity of closely arranged speakers (as in Wave Field Synthesis Arrays). Typical uses are spatial, timbral biasing of textures towards different areas of a speaker array, panning timbral modulations of a texture across a speaker array, and generally introducing various types of interior texture motion. There are two groups of classes included – arrays and patterns. The former generates arrays of values. The latter does the same, but as patterns, to be used together with e.g., Pbind or Pmono.

Installation

1) download the Parameter Distribution folder
2) download and install SuperCollider if you haven't already: https://supercollider.github.io/download
3) put the Parameter Distribution folder in ~Library/Application Support/SuperCollider/Extensions

Licence

The Parameter Distribution classes are free software, written by Erik Nyström, and published under GNU General Public License, GPLv3 in 2018.
