package tcm.com.gistone.controller;

import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tcm.com.gistone.database.entity.Section;
import tcm.com.gistone.database.entity.Word;
import tcm.com.gistone.database.entity.WordRelation;
import tcm.com.gistone.database.entity.WsRelation;
import tcm.com.gistone.database.mapper.*;
import tcm.com.gistone.util.ClientUtil;
import tcm.com.gistone.util.EdatResult;
import tcm.com.gistone.util.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class WordController {

	@Autowired
	private SectionMapper sm;
	@Autowired
	private WordMapper wm;
	@Autowired
	private WsRelationMapper wsm;
	@Autowired
	private WordRelationMapper wrm;
	@Autowired
	private ThemeMapper tm;

	@RequestMapping(value = "/word/recordWord", method = RequestMethod.POST)
	public EdatResult recordWord(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ClientUtil.SetCharsetAndHeader(request, response);
			JSONObject data = JSONObject.fromObject(request
					.getParameter("data"));
			String word = "";
			long parentId = 1;
			String themeType = "";
			Word w = wm.selectByWord(word);
			Word nw = new Word();
			nw.setParentId(parentId);
			nw.setThemeType(themeType);
			nw.setWord(word);
			if (w != null) {
				if (w.getThemeType().equals(themeType)) {
					return EdatResult.build(0, "录入失败，关键词重复");
				} else {
					wm.insert(nw);
					return EdatResult.build(1, "录入成功");
				}
			} else {
				wm.insert(nw);
				return EdatResult.build(1, "录入成功");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return EdatResult.build(0, "fail");
		}
	}

	@RequestMapping(value = "/word/deleteWord", method = RequestMethod.POST)
	public EdatResult deleteWord(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ClientUtil.SetCharsetAndHeader(request, response);
			JSONObject data = JSONObject.fromObject(request
					.getParameter("data"));
			long wordId = data.getLong("wordId");
			wm.deleteByPrimaryKey(wordId);
			return EdatResult.build(1, "删除成功");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return EdatResult.build(0, "fail");
		}
	}

	@RequestMapping(value = "/word/selectWord", method = RequestMethod.POST)
	public EdatResult selectWord(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ClientUtil.SetCharsetAndHeader(request, response);
			JSONObject data = JSONObject.fromObject(request
					.getParameter("data"));
			String themeType = data.getString("themeType");
			String word = data.getString("word");
			List<Word> list = wm.selectByType(themeType);
			Map map = new HashMap();
			map.put("data", list);
			map.put("num", list.size());
			return EdatResult.build(1, "success");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return EdatResult.build(0, "fail");
		}
	}

	public void recordWords() throws IOException {
		String path = "D:/zydata/中医古籍知识库-示例数据/示例数据/术语词典";
		/*
		 * String path="D:/zydata/中医古籍知识库-示例数据/示例数据/术语词典"; File file = new
		 * File(path); File[] list =file.listFiles(); for(int
		 * i=0;i<list.length;i++){ recordingWords(list[i].getAbsolutePath()); }
		 */
		recordingWords(path + "/病因病机.xlsx", "by");
		recordingWords(path + "/方剂.xlsx", "fj");
		recordingWords(path + "/疾病.xlsx", "jb");
		recordingWords(path + "/医籍.xlsx", "yj");
		recordingWords(path + "/医家.xlsx", "ya");
		recordingWords(path + "/证候.xlsx", "zh");
		recordingWords(path + "/症状.xlsx", "zz");
		recordingWords(path + "/治法.xlsx", "zf");
		recordingWords(path + "/中药.xlsx", "zy");
	}

	public void recordingWords(String path, String type) throws IOException {
		Workbook book = null;
		try {

			book = ExcelUtil.getExcelWorkbook(path);
			Sheet sheet = book.getSheetAt(0);
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();
			long num = 1;
			if (wm.selectMaxId() != null) {
				num = wm.selectMaxId() + 1;
			}
			for (int i = firstRowNum + 1; i < lastRowNum; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					int first = row.getFirstCellNum();
					// int last = row.getLastCellNum();
					Cell cell = row.getCell(first);
					if (cell != null
							&& cell.getCellType() != Cell.CELL_TYPE_BLANK) {
						Word word = new Word();
						word.setWordId(num);
						word.setParentId(num);
						word.setWord(ExcelUtil.getCellValue(row.getCell(0)));
						word.setThemeType(type);
						wm.insert(word);
						num++;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			book.close();
		}
	}

	public void recordWordRelation() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("by", 1);
		map.put("fj", 2);
		map.put("jb", 3);
		map.put("yj", 4);
		map.put("ya", 5);
		map.put("zh", 6);
		map.put("zz", 7);
		map.put("zf", 8);
		map.put("zy", 9);
		long t1 = System.currentTimeMillis();
		List<Section> list = sm.selectByBookId((long) 2);
		for (Section sec : list) {
			List<WsRelation> list1 = wsm.selectBySId(sec.getSectionId());
			for (int i = 0; i < list1.size(); i++) {
				WsRelation wr1 = list1.get(i);
				for (int j = i + 1; j < list1.size(); j++) {
					WsRelation wr2 = list1.get(j);
					int type1 = map.get(wm.selectTypeById(wr1.getWordId()));
					int type2 = map.get(wm.selectTypeById(wr2.getWordId()));
					if (type1 != type2) {
						WordRelation wr = new WordRelation();
						int num = wr1.getWordNum() > wr2.getWordNum() ? wr1
								.getWordNum() : wr2.getWordNum();
						if (type1 < type2) {
							WordRelation w = wrm.getByWords(type1, type2,
									wr1.getWordId(), wr2.getWordId());
							wr.setWordId(wr1.getWordId());
							wr.setAnoWordId(wr2.getWordId());
							wr.setNum(num);
							if (w == null) {
								wrm.insert(type1, type2, wr);
							} else {
								int num1 = w.getNum();
								w.setNum(num1 + num);
							}
						} else {
							WordRelation w = wrm.getByWords(type2, type1,
									wr2.getWordId(), wr1.getWordId());
							wr.setWordId(wr2.getWordId());
							wr.setAnoWordId(wr1.getWordId());
							wr.setNum(num);
							if (w == null) {
								wrm.insert(type1, type2, wr);
							} else {
								int num1 = w.getNum();
								w.setNum(num1 + num);
							}
						}
					}
				}
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}
}
