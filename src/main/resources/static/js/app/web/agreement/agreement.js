$(function() {
	//执行一个laydate实例
	laydate.render({
	  elem: '#signDate' //指定元素
	});
    var settings = {
        url: ctx + "agreement/list",
        pageSize: 10,
        queryParams: function(params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                timeField: $(".agreement-table-form").find("input[name='timeField']").val().trim(),
                type: $(".agreement-table-form").find("select[name='type']").val().trim(),
                val: $(".agreement-table-form").find("input[name='val']").val().trim(),
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'agreementId',
            title: '序号'
        }, {
            field: 'agreementNo',
            title: '协议编号'
        }, {
            field: 'userName',
            title: '姓名'
        }, {
            field: 'phone',
            title: '手机'
        }, {
            field: 'idNum',
            title: '身份证'
        }, {
            field: 'approvalAmount',
            title: '审批金额'
        },{
            field: 'borrowAmount',
            title: '借款金额'
        }, {
            field: 'creTime',
            title: '创建时间'
        },{
        	title: '操作',
            formatter: function(value, row, index) {
            	if(row.agreementNo != null) return "<a href='#' onclick='onSubmit(\"" + row.agreementId + "\",\"" + row.state + "\")'>提交</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                		"<a href='#' onclick='exportPDF(\"" + row.agreementId + "\",\"" + row.state + "\")'>导出</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                		"<a href='#' onclick='check(\"" + row.agreementId + "\")'>预览</a>";
            }
        }
        ]
    }
    $MB.initTable('agreementTable', settings);
    $MB.calenders('input[name="timeField"]',true,false);
    
});

function search() {
    $MB.refreshTable('agreementTable');
}

function refresh() {
    $(".agreement-table-form")[0].reset();
    $MB.refreshTable('agreementTable');
}

function deleteAgreements() {
    var selected = $("#agreementTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的协议！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].agreementId;
        if (i != (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "删除选中的协议，确定删除？",
        confirmButtonText: "确定删除"
    }, function() {
        $.post(ctx + 'agreement/delete', { "ids": ids }, function(r) {
            if (r.code == 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportAgreementExcel(){
	$.post(ctx+"agreement/excel",$(".agreement-table-form").serialize(),function(r){
		if (r.code == 0) {
			window.location.href = "common/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportAgreementCsv(){
	$.post(ctx+"agreement/csv",$(".agreement-table-form").serialize(),function(r){
		if (r.code == 0) {
			window.location.href = "common/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}
function onSubmit(id,state) {
	if (state == "1") {
		$MB.n_warning("已经提交，无需再次重复提交操作！！！");
		return;
	}
	$.get(ctx + "agreement/onSubmit", {"id": id}, function(r) {
		if (r.code == 0) {
			$MB.n_success('提交成功！');
            $MB.refreshTable('agreementTable');
		} else {
			$MB.n_danger(r.msg);
		}
	}, "json");
}
function exportPDF(id,state) {
	if (state == "2") {
		$MB.n_warning("此协议未提交状态，不能进行导出操作！！！");
		return;
	}
	window.location.href="agreement/exportPDF?id="+id;
}
function check(id) {
	$.get(ctx + "agreement/check", {"id": id}, function(r) {
		if (r.code == 0) {
			window.open(r.msg);
		} else {
			$MB.n_danger(r.msg);
		}
	}, "json");
}