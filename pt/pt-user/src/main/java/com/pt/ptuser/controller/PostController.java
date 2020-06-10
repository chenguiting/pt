package com.pt.ptuser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pt.ptcommon.util.R;
import com.pt.ptcommon.util.SecurityUtils;
import com.pt.ptuser.entity.SysPost;
import com.pt.ptuser.service.SysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wl
 * @date 2020/5/27
 */
@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private SysPostService sysPostService;

    /**
     * 获取岗位列表
     */

    @GetMapping("/list")
    public R list(Page page, SysPost post)
    {
        return R.ok( sysPostService.getPostPage(page,post));
    }


//    @GetMapping("/export")
//    public R export(SysPost post)
//    {
//        List<SysPost> list = sysPostService.selectPostList(post);
//        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
//        return util.exportExcel(list, "岗位数据");
//    }

    /**
     * 根据岗位编号获取详细信息
     */

    @GetMapping(value = "/{postId}")
    public R getInfo(@PathVariable String postId)
    {
        return R.ok(sysPostService.selectPostById(postId));
    }

    /**
     * 新增岗位
     */

    @PostMapping
    public R add( @RequestBody SysPost post)
    {
        if (!sysPostService.checkPostNameUnique(post))
        {
            return R.failed("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        else if (!sysPostService.checkPostCodeUnique(post))
        {
            return R.failed("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(SecurityUtils.getNickName());
        return R.ok(sysPostService.insertPost(post));
    }

    /**
     * 修改岗位
     */

    @PutMapping
    public R edit(@Validated @RequestBody SysPost post)
    {
        if (!sysPostService.checkPostNameUnique(post))
        {
            return R.failed("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        else if (!sysPostService.checkPostCodeUnique(post))
        {
            return R.failed("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(SecurityUtils.getNickName());
        return R.ok(sysPostService.updatePost(post));
    }

    /**
     * 删除岗位
     */

    @DeleteMapping("/{postIds}")
    public R remove(@PathVariable String[] postIds)
    {
        return R.ok(sysPostService.deletePostByIds(postIds));
    }

    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/optionselect")
    public R optionselect()
    {
        List<SysPost> posts = sysPostService.selectPostAll();
        return R.ok(posts);
    }
}
