OSSAccessKeyId = ''
url = ''
policy = ''
signature = ''
successActionStatus = ''
callback = ''
dir = ''

function send_request() {
    var xmlhttp = null;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    if (xmlhttp != null) {
        xmlhttp.open("GET", "/upload/credentials", false);
        xmlhttp.send(null);
        return xmlhttp.responseText
    }
    else {
        alert("Your browser does not support XMLHTTP.");
    }
};

function get_signature() {
    body = send_request()
    var obj = eval("(" + body + ")");
    url = obj['url']
    policy = obj['policy']
    OSSAccessKeyId = obj['OSSAccessKeyId']
    signature = obj['signature']
    successActionStatus = obj['successActionStatus']
    callback = obj['callback']
    dir = obj['dir']
    return true;
};

function set_upload_param(up, filename, ret) {
    if (ret == false) {
        ret = get_signature()
    }

    up.setOption({
        'url': url,
        'multipart_params': {
            'key': dir + filename,
            'policy': policy,
            'OSSAccessKeyId': OSSAccessKeyId,
            'success_action_status': successActionStatus,
            'callback': callback,
            'signature': signature,
        }
    });

    up.start();
}

var uploader = new plupload.Uploader({
    runtimes: 'html5,flash,silverlight,html4',
    browse_button: 'selectfiles',
    //multi_selection: false,
    container: document.getElementById('container'),
    flash_swf_url: 'lib/plupload-2.1.2/js/Moxie.swf',
    silverlight_xap_url: 'lib/plupload-2.1.2/js/Moxie.xap',
    url: 'http://oss.aliyuncs.com',

    filters: {
        mime_types : [ //只允许上传图片和zip文件
            { title : "Image files", extensions : "jpg,gif,png,bmp" },
            { title : "Zip files", extensions : "zip" }
        ],
        max_file_size : '400kb', //最大只能上传400kb的文件
        prevent_duplicates : true //不允许选取重复文件
    },

    init: {
        PostInit: function () {
            document.getElementById('ossfile').innerHTML = '';
            document.getElementById('postfiles').onclick = function () {
                set_upload_param(uploader, '', false);
                return false;
            };
        },

        FilesAdded: function (up, files) {
            plupload.each(files, function (file) {
                document.getElementById('ossfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                    + '<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
                    + '</div>';
            });
        },

        BeforeUpload: function (up, file) {
            set_upload_param(up, file.name, true);
        },

        UploadProgress: function (up, file) {
            var d = document.getElementById(file.id);
            d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
            var prog = d.getElementsByTagName('div')[0];
            var progBar = prog.getElementsByTagName('div')[0]
            progBar.style.width = 2 * file.percent + 'px';
            progBar.setAttribute('aria-valuenow', file.percent);
        },

        FileUploaded: function (up, file, info) {
            if (info.status == 200) {
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = 'upload to oss success';
            }
            else {
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
            }
        },

        Error: function (up, err) {
            document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
        }
    }
});

uploader.init();
