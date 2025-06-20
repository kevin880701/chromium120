(function () {
    let homePageUrl = "ub://newtab";
    let fetchDefaultMilliseconds = 1000;
    let failedRequestWaitingMilliseconds = fetchDefaultMilliseconds + 7000;

    let intervalId = setInterval(fetchHomePage, fetchDefaultMilliseconds);

    delayGoingHomePage(failedRequestWaitingMilliseconds);

    // @ts-ignore
    async function fetchHomePage() {
        let startFetchTimeStamp = new Date().getTime();
        let iconURL = homePageUrl + "/favicon.ico";//降低 server 負擔

        try {
            //連線只要有讀取到 url 的服務都算在此範圍，即使是 404 亦或 500 也是。
            //https://developer.mozilla.org/zh-TW/docs/Web/API/Fetch_API/Using_Fetch
            let response = await fetch(
                iconURL, {
                    method: 'GET',
                    mode: 'no-cors',
                    headers: {
                        'content-type': 'text/plain'
                    }
                }
            );

            let requestOk = (typeof response === "object");

            if (requestOk) {
                clearInterval(intervalId);
                let currentTimeStamp = new Date().getTime();
                let fetchMilliseconds = currentTimeStamp - startFetchTimeStamp;
                if (fetchMilliseconds < fetchDefaultMilliseconds) {
                    let delayMilliseconds = fetchDefaultMilliseconds - fetchMilliseconds;
                    delayGoingHomePage(delayMilliseconds);
                } else {
                    goHomePage();
                }
            }
        } catch (error) {
        }
    }

    function delayGoingHomePage(milliseconds: number) {
        setTimeout(() => {
            goHomePage();
        }, milliseconds);
    }

    function goHomePage() {
        window.location.href = homePageUrl;
    }
})();