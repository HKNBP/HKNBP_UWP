/*
 * HKNBP_UWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HKNBP_UWP is distributed in the hope that it will be useful,
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HKNBP_UWP.  If not, see <https://www.gnu.org/licenses/>.
 */

(function () {
	"use strict";

	var app = WinJS.Application;
	var activation = Windows.ApplicationModel.Activation;
    var isFirstActivation = true;

    // 初始Window大細
    var view = Windows.UI.ViewManagement.ApplicationView.getForCurrentView();
    view.tryResizeView({ height: 405, width: 720 });
    Windows.UI.ViewManagement.ApplicationView.preferredLaunchWindowingMode = Windows.UI.ViewManagement.ApplicationViewWindowingMode.preferredLaunchViewSize;
    Windows.UI.ViewManagement.ApplicationView.preferredLaunchViewSize.height = 405;
    Windows.UI.ViewManagement.ApplicationView.preferredLaunchViewSize.width = 720;

	app.onactivated = function (args) {
		if (args.detail.kind === activation.ActivationKind.voiceCommand) {
			// TODO: 處理相關 ActivationKinds。例如，如果您的應用程式可以透過語音命令啟動，
			//這是一個決定填入輸入欄位還是選擇不同初始檢視的好地方。
		}
		else if (args.detail.kind === activation.ActivationKind.launch) {
			// 使用者透過磚啟動您的應用程式時，會發生「啟動」啟用，
			//或者按一下或點選本文來叫用快顯通知。
			if (args.detail.arguments) {
				// TODO: 如果應用程式支援快顯通知，請使用快顯通知承載中的這個值，以決定在應用程式中的哪個位置
				//讓使用者叫用快顯通知來回應它們。
			}
			else if (args.detail.previousExecutionState === activation.ApplicationExecutionState.terminated) {
				// TODO: 已暫停並終止這個應用程式來取回記憶體。
				// 若要建立流暢的使用者體驗，請在此還原應用程式狀態，以便讓應用程式看起來像是從未停止執行一樣。
				// 注意: 建議您記錄最後暫停應用程式的時間，而且只在短時間之後返回時才還原狀態。
			}
		}

		if (!args.detail.prelaunchActivated) {
			// TODO: 如果 prelaunchActivated 為 true，則表示基於最佳化已在背景預先啟動應用程式。
			// 在該情況下，它會在之後短暫暫停。
			// 啟動時發生的任何長時間執行作業 (如高度耗費資源的網路或磁碟 I/O) 或使用者狀態變更，
			//皆應該在這裡完成 (避免在預先啟動情況下加以執行)。
			// 或者，此工作可以在 resume 或 visibilitychanged 處理常式中進行。
		}

		if (isFirstActivation) {
			// TODO: 已啟用但尚未執行應用程式。請在這裡執行一般啟動初始設定。
			document.addEventListener("visibilitychange", onVisibilityChanged);
            args.setPromise(WinJS.UI.processAll());


            /********
             * 開始 *
             ********/

            //同HKNBP_Core溝通程序
            var hknbpCoreIframe = document.getElementById("HKNBP_Core");

            var hknbpCoreIframeIsLoaded = false;

            hknbpCoreIframe.addEventListener("load", function () {
                hknbpCoreIframeIsLoaded = true;
                callHKNBPCoreIframe("confirmHKNBPCoreLoaded()");
            });

            function setOnHKNBPCoreIframeLoaded(onLoaded) {
                if (hknbpCoreIframeIsLoaded === true) {
                    onLoaded();
                } else {
                    hknbpCoreIframe.addEventListener("load", function () { onLoaded(); });
                }
            }

            function callHKNBPCoreIframe(expr) {
                setOnHKNBPCoreIframeLoaded(function () {
                    var caller = {};
                    caller.name = "HKNBP_App";
                    caller.expr = expr;
                    hknbpCoreIframe.contentWindow.postMessage(JSON.stringify(caller), "*")
                });
            }

            window.addEventListener("message", function (event) {
                var callMessage = {};
                try { callMessage = JSON.parse(event.data); } catch (e) { }
                if (callMessage.name === "HKNBP_Core") {
                    hknbpCoreIframeIsLoaded = callMessage.message;
                } else {
                    callHKNBPCoreIframe(event.data);
                }
            }, false)


            //設定
            var hknbpUWPAppVersion = "0.9-UWP";
     
            callHKNBPCoreIframe("hknbpCore.appVersion = \"" + hknbpUWPAppVersion + "\"");


            //手制控制
            var remotePath = "hknbpCorePath.VirtualRemote";
            document.addEventListener('keypress', function (e) {
                switch (e.keyCode) {
                    case 211:  // GamepadLeftThumbstickUp
                    case 203:  // GamepadDPadUp
                        callHKNBPCoreIframe("hknbpCore.upButton.click()");
                        break;
                    case 212:  // GamepadLeftThumbstickDown
                    case 204:  // GamepadDPadDown
                        callHKNBPCoreIframe("hknbpCore.downButton.click()");
                        break;
                    case 214:  // GamepadLeftThumbstickLeft
                    case 205:  // GamepadDPadLeft
                        callHKNBPCoreIframe("hknbpCore.leftButton.click()");
                        break;
                    case 213:  // GamepadLeftThumbstickRight
                    case 206:  // GamepadDPadRight
                        callHKNBPCoreIframe("hknbpCore.rightButton.click()");
                        break;
                    case 195:  // A Button
                        callHKNBPCoreIframe("hknbpCore.centerButton.click()");
                        break;
                    case 196: // B button
                        callHKNBPCoreIframe("hknbpCore.tvChannelDescriptionButton.click()");
                        break;
                    case 197: // X Button
                        callHKNBPCoreIframe("hknbpCore.volumeMuteButton.click()");
                        break;
                    case 198: // Y Button
                        callHKNBPCoreIframe("hknbpCore.epgButton.click()");
                        break;
                    case 208: // View button
                        break;
                    case 207: // Menu button
                        callHKNBPCoreIframe("hknbpCore.menuButton.click()");
                        break;
                    case 200: // Left Bumper
                        callHKNBPCoreIframe("hknbpCore.nextAudioButton.click()");
                        break;
                    case 201: // Left Trigger
                        callHKNBPCoreIframe("hknbpCore.nextSubtitleButton.click()");
                        break;
                    case 199: // Right Bumper
                        callHKNBPCoreIframe("hknbpCore.nextChannelButton.click()");
                        break;
                    case 202: // Right Trigger
                        callHKNBPCoreIframe("hknbpCore.previousChannelButton.click()");
                        break;
                }
            });
		}

		isFirstActivation = false;
	};

	function onVisibilityChanged(args) {
		if (!document.hidden) {
			// TODO: 應用程式剛剛變成可見。這可能是重新整理檢視的好時機。
		}
	}

	app.oncheckpoint = function (args) {
		// TODO: 此應用程式即將暫停。請在這裡儲存必須在暫停期間保留的所有狀態。
		//您可以使用 WinJS.Application.sessionState 物件，此物件會自動儲存並在暫停期間還原。
		//若您需要在應用程式暫停之前先完成非同步作業，請呼叫 args.setPromise()。
	};

	app.start();

})();