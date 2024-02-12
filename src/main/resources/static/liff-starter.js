liff.use(
  new LIFFInspectorPlugin({ origin: "wss://c381-60-89-9-17.ngrok-free.app" })
);

window.onload = function (e) {
  liff
    .init({
      liffId: "1661125627-aBoALjjK",
    })
    .then(function () {
      initializeApp();
    })
    .catch(function (error) {
      console.error("LIFF initialization failed: " + error);
    });
};

function initializeApp() {
  document.getElementById("languageld").textContent = liff.getLanguage();
  document.getElementById("typeld").textContent = liff.getContext().type;
  document.getElementById("endpointUrlld").textContent =
    liff.getContext().endpointUrl;
  document.getElementById("OSld").textContent = liff.getOS();
  document.getElementById("versionld").textContent = liff.getVersion();
  document.getElementById("lineVersionld").textContent = liff.getLineVersion();

  // openWindow call
  document
    .getElementById("openwindowbutton")
    .addEventListener("click", function () {
      liff.openWindow({
        url: "https://line.me",
      });
    });

  // closeWindow call
  document
    .getElementById("closewindowbutton")
    .addEventListener("click", function () {
      liff.closeWindow();
    });

  // sendMessages call
  document
    .getElementById("sendtestbutton")
    .addEventListener("click", function () {
      liff
        .sendMessages([
          {
            type: "text",
            text: "You've successfully sent a message! Hooray!",
          },
          {
            type: "sticker",
            packageId: "11537",
            stickerId: "52002734",
          },
        ])
        .then(function () {
          window.alert("Message sent");
        })
        .catch(function (error) {
          window.alert("Error sending message: " + error);
        });
    });

  // get access token
  document
    .getElementById("getaccesstoken")
    .addEventListener("click", function () {
      const accessToken = liff.getContext().accessTokenHash;
      document.getElementById("accesstokenfield").textContent = accessToken;
      toggleAccessToken();
    });

  // get profile call
  document
    .getElementById("getprofilebutton")
    .addEventListener("click", function () {
      liff
        .getProfile()
        .then(function (profile) {
          document.getElementById("useridprofilefield").textContent =
            profile.userId;
          document.getElementById("displaynamefield").textContent =
            profile.displayName;

          const profilePictureDiv =
            document.getElementById("profilepicturediv");
          if (profilePictureDiv.firstElementChild) {
            profilePictureDiv.removeChild(profilePictureDiv.firstElementChild);
          }
          const img = document.createElement("img");
          img.src = profile.pictureUrl;
          img.alt = "Profile Picture";
          profilePictureDiv.appendChild(img);

          document.getElementById("statusmessagefield").textContent =
            profile.statusMessage;
          toggleProfileData();
        })
        .catch(function (error) {
          window.alert("Error getting profile: " + error);
        });
    });

  // sendtegamibutton call
  document
    .getElementById("sendmessagebutton")
    .addEventListener("click", function (e) {
      e.preventDefault(); // フォームのデフォルトの送信を防止
      const messageText = document.getElementById("message").value;
      const messageLength = messageText.length;
      const userId = "";
      const userName = "";
      const message = "";
      const password = "";

      // 条件をチェックしてアラートを表示
      // if (messageLength < 300) {
      //   alert("テキストエリア内の文字数が300文字未満です。");
      //   return;
      // }

      // ユーザーID, ユーザー名、取得
      liff.getProfile().then(function (profile) {
        userId = profile.userId;
        userName = profile.displayName;
        message = userName + "さんから手紙が届きました。";
      });

      // insertMessage(userId, messageText).then((returnedPassword) => {
      //   password = returnedPassword;
      //   message = message + "\\n パスワードはこちら:" + password;
      // });

      if (liff.isApiAvailable("shareTargetPicker")) {
        liff.shareTargetPicker([
          {
            type: "text",
            text: message,
          },
        ]);
      } else {
        console.log(
          "can not use sharetargetPicker. please check your liffapp settings"
        );
      }
    });
}

function toggleAccessToken() {
  toggleElement("accesstokendata");
}

function toggleProfileData() {
  toggleElement("profileinfo");
}

function toggleElement(elementId) {
  const elem = document.getElementById(elementId);
  if (elem.offsetWidth > 0 && elem.offsetHeight > 0) {
    elem.style.display = "none";
  } else {
    elem.style.display = "block";
  }
}

function insertMessage(userId, message) {
  const url = "https://1f69-60-89-9-17.ngrok-free.app/insertMessage";
  userId = "Ub6f9ed76f7957f2d505542df8b706a95";

  const data = {
    lineUserId: userId,
    messageText: message,
  };

  const options = {
    mothod: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  };

  fetch(url, options)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json(); // レスポンスをJSON形式で解析
    })
    .then((data) => {
      console.log("Response data:", data); // レスポンスデータをログに出力
      if (data.password) {
        return data.password; // パスワードを戻り値として返す
      } else {
        throw new Error("Password not found in the response data");
      }
    })
    .catch((error) => {
      console.error("There was a problem with the fetch operation:", error);
    });
}
