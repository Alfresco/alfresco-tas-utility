<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="Browse Images">
    <meta name="author" content="${author}">

    <title>Browse Images</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet">

    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style>
        body {
            background-color: linen;
            padding: 50px;
        }
        
        * {
            box-sizing: border-box;
        }
        
        .row>.column {
            padding: 0 8px;
        }
        
        .row:after {
            content: "";
            display: table;
            clear: both;
        }
        
        .column {
            float: left;
            width: 25%;
        }
        /* The Modal (background) */
        
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            padding-top: 100px;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: black;
        }
        /* Modal Content */
        
        .modal-content {
            position: relative;
            background-color: #fefefe;
            margin: auto;
            padding: 0;
            width: 90%;
            max-width: 1200px;
        }
        /* The Close Button */
        
        .close {
            color: white;
            position: absolute;
            top: 10px;
            right: 25px;
            font-size: 35px;
            font-weight: bold;
        }
        
        .close:hover,
        .close:focus {
            color: #999;
            text-decoration: none;
            cursor: pointer;
        }
        
        .mySlides {
            display: none;
        }
        
        .cursor {
            cursor: pointer
        }
        /* Next & previous buttons */
        
        .prev,
        .next {
            cursor: pointer;
            position: absolute;
            top: 50%;
            width: auto;
            padding: 16px;
            margin-top: -50px;
            color: white;
            font-weight: bold;
            font-size: 20px;
            transition: 0.6s ease;
            border-radius: 0 3px 3px 0;
            user-select: none;
            -webkit-user-select: none;
        }
        /* Position the "next button" to the right */
        
        .next {
            right: 0;
            border-radius: 3px 0 0 3px;
        }
        /* On hover, add a black background color with a little bit see-through */
        
        .prev:hover,
        .next:hover {
            background-color: rgba(0, 0, 0, 0.8);
        }
        /* Number text (1/3 etc) */
        
        .numbertext {
            color: #f2f2f2;
            font-size: 12px;
            padding: 8px 12px;
            position: absolute;
            top: 0;
        }
        
        img {
            margin-bottom: -4px;
        }
        
        .caption-container {
            text-align: center;
            background-color: black;
            padding: 2px 16px;
            color: white;
        }
        
        .demo {
            opacity: 0.6;
        }
        
        .active,
        .demo:hover {
            opacity: 1;
        }
        
        img.hover-shadow {
            transition: 0.3s
        }
        
        .hover-shadow:hover {
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)
        }
    </style>
</head>

<body>

    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
                <a class="navbar-brand" href="#">Browse Images</a>
            </div>
            <!--/.nav-collapse -->
        </div>
    </nav>

    <div class="container">
        <#list osDataFiles as osData>
            <div class="col-sm-3">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>${osData.getOsName()} (${osData.getFilesCount()})</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list osData.getFiles() as osFiles>
                            <tr>

                                <td width="10%">
                                    <p>${osFiles.getName()}</p>
                                    <a href="#" class="imagePop" data-toggle="modal" data-target="#myModal">
                                        <img src="${osFiles.toString()}" alt="${osFiles.getName()}" width="50%">
                                    </a>
                                </td>

                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </#list>
    </div>


    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Modal title</h4>
                </div>
                <div class="modal-body">
                    <img src="" class="imageModal">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>


    <script>
        $(function() {
            $('.imagePop').on('click', function() {
                $('.imageModal').attr('src', $(this).find('img').attr('src'));
            });
        });
    </script>
</body>

</html>