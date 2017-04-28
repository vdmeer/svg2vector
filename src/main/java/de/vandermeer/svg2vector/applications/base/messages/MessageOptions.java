/* Copyright 2017 Sven van der Meer <vdmeer.sven@mykolab.com>
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

package de.vandermeer.svg2vector.applications.base.messages;

import de.vandermeer.svg2vector.applications.core.CliOptionPackage;

/**
 * Application message options for errors, warnings, progess information, and detailed information.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public final class MessageOptions extends CliOptionPackage {

	/** Print option quiet. */
	public final static int OPTION_QUIET = 0b000;

	/** Print option error. */
	public final static int OPTION_ERROR = 0b0001;

	/** Print option warning. */
	public final static int OPTION_WARNING = 0b0010;

	/** Print option progress. */
	public final static int OPTION_PROGRESS = 0b0100;

	/** Print option details. */
	public final static int OPTION_DEAILS = 0b1000;

	/** Print option verbose. */
	public final static int OPTION_VERBOSE = OPTION_ERROR | OPTION_WARNING | OPTION_PROGRESS | OPTION_DEAILS;

	/** Application option for verbose mode. */
	private final AO_VerboseExt optVerbose = new AO_VerboseExt();

	/** Application option for quiet mode. */
	private final AO_QuietExt optQuiet = new AO_QuietExt();

	/** Application option for printing progress information. */
	private final AO_MsgProgress optMsgProgress = new AO_MsgProgress();

	/** Application option for printing warning messages. */
	private final AO_MsgWarnings optMsgWarning = new AO_MsgWarnings();

	/** Application option for printing detailed messages. */
	private final AO_MsgDetail optMsgDetail = new AO_MsgDetail();

	/** Application option to switch off error messages. */
	private final AO_NoErrors optNoErrors = new AO_NoErrors();

	/** Message mode for the application, 0 is quiet, all other values are generated using message type bit masks. */
	private int msgMode = OPTION_ERROR;

	/**
	 * Creates a new message options object with application options loaded.
	 */
	public MessageOptions(){
		this.setSimpleOptions(
			this.optVerbose,
			this.optQuiet,
			this.optMsgProgress,
			this.optMsgWarning,
			this.optMsgDetail,
			this.optNoErrors
		);
	}

	/**
	 * Sets the message mode, requires the options to be set with CLI values.
	 */
	public void setMessageMode(){
		if(this.optQuiet.inCli()){
			this.msgMode = OPTION_QUIET;
			return;
		}
		if(this.optVerbose.inCli()){
			this.msgMode = OPTION_VERBOSE;
			return;
		}

		if(this.optMsgProgress.inCli()){
			this.msgMode = this.msgMode | OPTION_PROGRESS;
		}
		if(this.optMsgWarning.inCli()){
			this.msgMode = this.msgMode | OPTION_WARNING;
		}
		if(this.optMsgDetail.inCli()){
			this.msgMode = this.msgMode | OPTION_DEAILS;
		}
		if(this.optNoErrors.inCli()){
			this.msgMode = this.msgMode &= ~OPTION_ERROR;
		}
	}

	/**
	 * Returns the message mode, default if not set.
	 * @return message mode
	 */
	public int getMessageMode(){
		return this.msgMode;
	}
}
