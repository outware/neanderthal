#Neanderthal

The purpose of Neanderthal is to allow you to create and modify configuration environments for Android applications at run time. 

This serves a similar purpose to setting build variables for different variants, however the benefit of Neanderthal is that configuration environments can be added, removed or updated live instead of requiring separate builds.

##Usage

1. Add `compile 'au.com.outware:neanderthal:<version>'` to your application's dependencies
2. Create an application class that extends from `NeanderthalApplication` and call `initialise` from within the applications `onCreate` function, supplying the class type of your configuration, the different variants and the default variant. For examples of how to do this, refer to `SampleApplication.java` in the `neanderthal-sample` module.
	
##License
		The MIT License (MIT)
		
		Copyright (c) 2015 Outware Mobile
		
		Permission is hereby granted, free of charge, to any person obtaining a copy
		of this software and associated documentation files (the "Software"), to deal
		in the Software without restriction, including without limitation the rights
		to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
		copies of the Software, and to permit persons to whom the Software is
		furnished to do so, subject to the following conditions:
		
		The above copyright notice and this permission notice shall be included in all
		copies or substantial portions of the Software.
		
		THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
		IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
		FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
		AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
		LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
		OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
		SOFTWARE.