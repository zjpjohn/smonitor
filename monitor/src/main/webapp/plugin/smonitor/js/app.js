/**
 *将可变对象转化成多个 input（html形式）
 * @param fields 字段list
 * @param fieldValMap 字段与值对应map
 * @returns html 此对象input属性的html串
 */
function getFieldsHtml(fields, fieldValMap) {
    var fhtml="";
    if (typeof(fieldValMap) == "undefined") {
        fieldValMap={};
    }
    $.each(fields,function(i,item){
        var val=fieldValMap[item.fieldName];
        if (typeof(val) == "undefined") {
            val="";
        }
        console.log(item);
        fhtml+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label>";
        fhtml+="<input value='"+val+"' placeholder='"+item.desc+"' type='text' class='form-control' name='"+item.fieldName+"'/></div>";
    });
    return fhtml;
};
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};