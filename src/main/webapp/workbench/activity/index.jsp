<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jqueyry/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		
		// 为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			/*
					操作模态窗口的方式：
						需要操作模态窗口的jQuery对象，调用model方法，为该方法传递参数：show : 打开窗口，hide:隐藏窗口。
			 */

			// 走后台，目的是为了取得用户信息列表，为所有者下拉框铺值
			$.ajax({
				url: "workbench/activity/getUserList.do",

				type: "get",
				dataType: "json",
				success: function (data) {
					/*
							List<User> uList
							data
								[{},{},{}]
					 */
					var html = "<option></option>";

					// 遍历出来的每一个n, 就是一个user对象
					$.each(data, function (i, n) {
						html += "<option value='"+n.id+"'>"+n.name+"</option>"
					})
					$("#create-owner").html(html);

					// 获取当前登录用户的id
					// 在js中使用el表达式，el表达式一定要套用在字符串中
					var id = "${user.id}";

					// 将当前登陆的选项，定为为下拉框默认的选项
					$("#create-owner").val(id);

					// 所有者下拉框处理完毕后，展现模态窗口
					$("#createActivityModal").modal("show");
				}
			})
		})

		// 为保存按钮绑定事件，执行添加操作
		$("#saveBtn").click(function () {

			$.ajax({
				url: "workbench/activity/save.do",
				data: {
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-starDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())
				},
				type: "post",
				dataType: "json",
				success: function (data) {
					/*
							{success: true/false}
					 */
					if (data.success) {
						//添加成功后
						// 刷新市场活动信息列表（局部刷新）
						pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
						// 清空添加操作模态窗口中的数据
						// 提交表单
						// $("#activityAddForm").submit();
						/*
							注意：
								我们拿到了form表单的jQuery对象
								对于表单jQuery对象，提供了submit()方法让我们提交表单
								但是表单的jQuery对象，没有为我们提供reset()方法让我们重置表单，（坑，idea为我们提示了有reset()方法）

								虽然jQuery对象没有提供reset()方法，但是原生的js为我们提供了reset方法
								所以我们要将jQuery对象转换为原生dom对象

								jQuery对象转dom对象：
									jquery对象[下标]
								dom对象如何转换为jQuery对象：
									$(dom)
						 */
						$("#activityAddForm")[0].reset();

						// 关闭模态窗口
						$("#createActivityModal").modal("hide");
					} else {
						alert("添加市场活动失败");
					}
				}
			})

		})
		// 页面加载完成后触发一个方法
		// 默认展开列表的第一页，每页展现两条记录
		pageList(1, 2);

		// 为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function () {
			// 将查询信息保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1, 2);
		})

		// 为全选框绑定事件
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked", this.checked);
		})
		// 为复选框绑定事件
		$("#activityBody").on("click", $("input[name=xz]"), function () {
			$("#qx").prop("checked", $("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

		// 为删除操作绑定事件
		$("#deleteBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if ($xz.length == 0) {
				alert("请选择需要删除的记录");
			} else {
				if (confirm("确定删除所选中的记录吗？")) {
					//url: workbench/activity/delete.do?
					//拼接参数
					var param = "";
					for (var i = 0;i < $xz.length;i++) {
						param += "id=" + $($xz[i]).val();
						if (i < $xz.length - 1) {
							param += "&";
						}
					}
					$.ajax({
						url : "workbench/activity/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function (data) {
							/*
                                {"success": true/false}
                             */
							if (data.success) {
								pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
							} else {
								alert("删除失败");
							}
						}
					})
				}
			}
		})

		// 为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function () {

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			var $xz = $("input[name=xz]:checked");

			if ($xz.length == 0) {
				alert("请选择需要修改的记录");
			} else if ($xz.length > 1) {
				alert("只能选择一条记录进行修改");
			//肯定选择了一条记录
			} else {
				var id = $xz.val();
				$.ajax({
					url : "workbench/activity/getUserListAndActivity.do",
					data : {
						"id" : id
					},
					type : "get",
					dataType : "json",
					success : function (data) {
						/*
							data :
								用户列表
								市场活动对象
								{"uList": [{用户1},{2},{3}],"a":{市场活动}}
						 */
						// 处理所以者的下拉框
						var html = "<option></option>";

						// 遍历出来的每一个n, 就是一个user对象
						$.each(data.uList, function (i, n) {
							html += "<option value='"+n.id+"'>"+n.name+"</option>"
						})
						$("#edit-owner").html(html);

						// 处理单条activity
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);

						// 所以信息都填写好后，打开市场活动修改的模态窗口
						$("#editActivityModal").modal("show");
					}
				})
			}
		})

        // 为修改的保存操作绑定事件
        /*
            在实际项目开发中，一般现做添加，再做修改
         */
        $("#updateBtn").click(function () {
            $.ajax({
                url: "workbench/activity/update.do",
                data: {
                    "id" : $.trim($("#edit-id").val()),
                    "owner": $.trim($("#edit-owner").val()),
                    "name": $.trim($("#edit-name").val()),
                    "startDate": $.trim($("#edit-startDate").val()),
                    "endDate": $.trim($("#edit-endDate").val()),
                    "cost": $.trim($("#edit-cost").val()),
                    "description": $.trim($("#edit-description").val())
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    /*
                            {success: true/false}
                     */
                    if (data.success) {
                        // 添加成功后
                        // 刷新市场活动信息列表（局部刷新）
                        // pageList(1, 2);
						/*
						 *	$("#activityPage").bs_pagination('getOption','currentPage')
						 *			操作后留在当前页
						 *
						 *	$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
						 *			操作后维持已经设置好的每页展现的记录数
						 **/
						pageList($("#activityPage").bs_pagination('getOption','currentPage')
								,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
                        // 关闭模态窗口
                        $("#editActivityModal").modal("hide");
                    } else {
                        alert("修改市场活动失败");
                    }
                }
            })
        })
	});

	/*
	    对于所有的关系型数据库，做前端的分页相关操作的组件
	    就是pageNo, pageSize
	    pageNo: 表示当前页的页码
	    pageSize: 每页展现的记录数

	    pageList方法： 页面加载完成后，调用该方法
	    哪些情况下，需要调用pageList方法
	    （1）点击左侧的“市场活动”,
	     (2) 添加、修改、删除后,
		 (3) 点击查询的时候,
		 (4) 点击分页组件的时候。
	 */
	function pageList(pageNo, pageSize) {

		$("#qx").prop("checked", false);
		// 将隐藏域中的信息保存到search域中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url: "workbench/activity/pageList.do",
			data: {
				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
			},
			type: "get",
			dataType: "json",
			success: function (data) {
				/*
					data:
						我们需要的市场活动信息列表
						[{市场活动1},{2},{3}] List<Activity> aList
						一会分页插件需要的： 查询出来的总记录数
						{"total": 100} int total

					{{"total": 100, "dataList": [{市场活动1},{2},{3}]}
				 */
				var html = "";
				$.each(data.dataList, function (i, n) {
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.owner+'</a></td>';
					html += '<td>'+n.name+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
				})
				$("#activityBody").html(html);

				// 计算总页数
				var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
				// 数据处理完毕后，结合插件来使用
				$("#activityPage").bs_pagination({
					currentPage : pageNo,
					rowsPerPage: pageSize,
					maxRowsPerPage: 20,
					totalPages: totalPages,
					totalRows: data.total,

					visiblePageLinks: 3,
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					// 该回调函数，点击分页组件的时候触发
					onChangePage : function (event, data) {
						pageList(data.currentPage, data.rowsPerPage);
					}

				});
			}
		})
	}
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityName" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-starDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>
						<div class="form-group">
							<label for="edit-marketActivityName" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls或.xlsx格式]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS/XLSX的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
						点击创建按钮，观察两个属性和属性值

						data-toggle="model":
							表示触发该按钮，将要打开一个模态窗口
						data-target="#createActivityModal"：
							表示打开哪个模态窗口，通过#id的形式打开
						我们是以属性和属性值的方式打开模态窗口，但这样做有问题：
							问题在于没有办法对按钮的功能进行扩充

						所以未来的实际项目开发，对于模态窗口的操作，一定不要写死在元素当中，
						通过js代码实现
					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>

		</div>
	</div>
</body>
</html>