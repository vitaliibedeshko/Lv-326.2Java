package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.OrderTableListDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.service.TableListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class TableListController {

    @Autowired
    private TableListService tableListService;

    @PostMapping("/board/{boardId}")
    public TableListDto createTableList(@PathVariable Long boardId, @RequestBody TableList tableList) {
        return tableListService.createTableList(boardId, tableList);
    }

    @DeleteMapping("/{listId}")
    public void deleteTableList(@PathVariable("listId") Long listId) {
        tableListService.deleteTableList(listId);
    }

    @PutMapping("/{listId}/board/{boardId}")
    public TableListDto updateTableList(@PathVariable("listId") Long listId, @PathVariable("boardId") Long boardId,
                                        @RequestBody TableList tableList) {
        return tableListService.updateTableList(listId, boardId, tableList);
    }

    @GetMapping("/board/{boardId}")
    public List<TableListDto> getAllTableLists(@PathVariable("boardId") Long boardId) {
        return tableListService.getAllTableLists(boardId);
    }

    @GetMapping("/{listId}")
    public TableListDto getTableList(@PathVariable("listId") Long listId) {
        return tableListService.getTableList(listId);
    }

    @PutMapping("/order")
    public void updateListOrdering(@RequestBody OrderTableListDto orderTableListDto) {
        tableListService.updateListOrdering(orderTableListDto);
    }
}