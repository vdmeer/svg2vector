/* Copyright 2014 Sven van der Meer <vdmeer.sven@mykolab.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * SVG to vector converter.
 * This tool converts an SVG graphic to a vector format. Currently supported are EMF, PDF and SVG.
 * The tool does support SVG and SVGZ input formats. It also can deal with SVG layers.
 * 
 * Input can be a file or a URL. Out put can be a single file or a set of files if handling SVG layers individually.
 * This means that the tool can be part of a tool chain for creating animations. Simply create an SVG image with several layers,
 * one per step of an animation. Then use this tool to create an image per layer and use in the target publication platform:
 * <ul>
 * 		<li>EMF - for animations in any Microsoft Office product, e.g. PowerPoint</li>
 * 		<li>PDF - for animations in the LaTeX package beamer</li>
 * 		<li>SVG - for animations using HTML pages and SVG images</li>
 * </ul>
 * 
 * <p>All images are created using a custom page size (PDF, SVG) or a custome image size (EMF). This means that the created image has the size
 * of the original image, even for PDFs. Margins are set to 0, so there are no extras around the converted image either.</p>
 * 
 * Input options (one is required):
 * <ul>
 * 		<li><code>-f,--file arg</code> = input file, SVG or SVGZ coded, Inkscape formats are supported</li>
 * 		<li><code>-u,--uri arg</code> = input URL, points to a file, SVG or SVGZ coded, Inkscape formats are supported</li>
 * </ul>
 *
 * Output options:
 * <ul>
 * 		<li><code>-t,--target arg</code> = target vector format, supported are: emf, pdf, svg</li>
 * 		<li><code>-o,--output arg</code> = output file if no special layer processing is activated, default is the base name of the input file</li>
 * 		<li><code>-d,--directory arg</code> = output directory, default is the current directory</li>
 * </ul>
 * 
 * Process Options:
 * <ul>
 * 		<li><code>-v,--verbose</code> = verbose mode, prints process of the conversion</li>
 * 		<li><code>-h,--help</code> = prints a help screen with all command line options</li>
 * </ul>
 * 
 * Render Options:
 * <ul>
 * 		<li><code>-b,--no-background</code> = switch off background</li>
 * 		<li><code>-c,--clip</code> = activate clipping</li>
 * 		<li><code>-n,--not-transparent</code> = switch of transpararency</li>
 * 		<li><code>-r,--bgrnd-color arg</code> = sets the background color</li>
 * 		<li><code>-s,--not-text-as-shape</code> = switch of text-as-shape property</li>
 * </ul>
 * 
 * Layer Options:
 * <ul>
 * 		<li><code>-l,--one-per-layer</code> = activates special process for layers creating one output file per layer</li>
 * 		<li><code>-i,--use-layer-index</code> = use layer index for SVG layer processing, default is layer ID</li>
 * 		<li><code>-I,--use-layer-index-id</code> = use layer index and ID for SVG layer processing, default is layer ID</li>
 * </ul>
 * 
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.0.2 build 150701 (01-Jul-15) for Java 1.7
 */
package de.vandermeer.svg2vector;