<html>
<body>

<h1>${message}</h1>


<script>
    function submitBonClick() {
        var head2 = document.getElementsByTagName("h1").item(0);
        head2.innerText("I changed!");
    }
</script>
<div>
    <textarea id="123" >no msg</textarea>
    </div>
<button id="submitB" onsubmit="" onclick="submitBonClick()">change me</button>

<script>
    var show = document.getElementById("123");
    if (${owner}) {show.innerHTML("<h2>Welcome ${owner}</h2>")};
    if (${table}) {show.innerHTML("<h2>here ${table}</h2>")};
</script>

</body>
</html>