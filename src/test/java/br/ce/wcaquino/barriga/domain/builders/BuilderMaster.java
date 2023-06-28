package br.ce.wcaquino.barriga.domain.builders;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.ce.wcaquino.barriga.domain.Conta;
import br.ce.wcaquino.barriga.domain.Usuario;

/**
 * Classe responsável pela criação de builders de entidades
 * 
 * @author wcaquino@gmail.com
 *
 */
public class BuilderMaster {

	Set<String> listaImports = new HashSet<String>();

	@SuppressWarnings("rawtypes")
	public void gerarCodigoClasse(Class classe) {

		String nomeClasse = classe.getSimpleName() + "Builder";

		StringBuilder builder = new StringBuilder();

		builder.append(format("public class %s {\n", nomeClasse));
		List<Field> declaredFields = getClassFields(classe).stream()
				.filter(campo -> !campo.getName().equals("serialVersionUID") && !Modifier.isStatic(campo.getModifiers()))
				.collect(Collectors.toList());
		declaredFields.forEach(campo -> {
			if (campo.getType().getSimpleName().equals("List")) 
				builder.append(format("\tprivate %s<%s> %s;\n", campo.getType().getSimpleName(), getGenericSimpleName(campo), campo.getName()));
			else
				builder.append(format("\tprivate %s %s;\n", campo.getType().getSimpleName(), campo.getName()));
		});

		builder.append(format("\n\tprivate %s(){}\n\n", nomeClasse));

		builder.append(format("\tpublic static %s um%s() {\n", nomeClasse, classe.getSimpleName()));
		builder.append(format("\t\t%s builder = new %s();\n", nomeClasse, nomeClasse));
		builder.append("\t\tinicializarDadosPadroes(builder);\n");
		builder.append("\t\treturn builder;\n");
		builder.append("\t}\n\n");

		builder.append(format("\tpublic static void inicializarDadosPadroes(%s builder) {\n", nomeClasse));
		declaredFields.forEach(campo -> builder.append(format("\t\tbuilder.%s = %s;\n", campo.getName(), getDefaultParameter(campo))));
		builder.append("\t}\n\n");

		for (Field campo : declaredFields) {
			registrarImports(campo.getType().getCanonicalName());
			if (campo.getType().getSimpleName().equals("List")) {
				registrarImports("java.util.Arrays");
				builder.append(format("\tpublic %s comLista%s%s(%s... %s) {\n", 
					nomeClasse, campo.getName().substring(0, 1).toUpperCase(), campo.getName().substring(1), getGenericSimpleName(campo), campo.getName()));
				builder.append(format("\t\tthis.%s = Arrays.asList(%s);\n", campo.getName(), campo.getName()));
			} else {
				builder.append(format("\tpublic %s com%s%s(%s %s) {\n",
					nomeClasse, campo.getName().substring(0, 1).toUpperCase(), campo.getName().substring(1), campo.getType().getSimpleName(), campo.getName()));
				builder.append(format("\t\tthis.%s = %s;\n", campo.getName(), campo.getName()));
			}
			builder.append("\t\treturn this;\n");
			builder.append("\t}\n\n");
		}

		builder.append(format("\tpublic %s agora() {\n", classe.getSimpleName()));
		builder.append(format("\t\treturn new %s(", classe.getSimpleName()));
		boolean first = true;
		for (Field campo : declaredFields) {
			if(first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(campo.getName());
		}
		builder.append(");\n\t}\n");

		builder.append("}");

		for (String str : listaImports) {
			if(!str.contains("java.lang."))
				System.out.println(str);
		}
		System.out.println(format("import %s;\n", classe.getCanonicalName()));
		System.out.println(builder.toString());
	}

	@SuppressWarnings("rawtypes")
	private String getGenericSimpleName(Field campo) {
		ParameterizedType stringListType = (ParameterizedType) campo.getGenericType();
		return ((Class) stringListType.getActualTypeArguments()[0]).getSimpleName();
	}

	@SuppressWarnings("rawtypes")
	public List<Field> getClassFields(Class classe) {
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(classe.getDeclaredFields()));
		Class superClass = classe.getSuperclass();
		if (superClass != Object.class) {
			List<Field> fieldsSC = Arrays.asList(superClass.getDeclaredFields());
			fields.addAll(fieldsSC);
		}
		return fields;
	}

	public String getDefaultParameter(Field campo) {
		String tipo = campo.getType().getSimpleName();
		if (tipo.equals("int") || tipo.equals("Integer")) {
			return "0";
		}
		if (tipo.equalsIgnoreCase("long")) {
			return "0L";
		}
		if (tipo.equalsIgnoreCase("double") || tipo.equalsIgnoreCase("float")) {
			return "0.0";
		}
		if (tipo.equalsIgnoreCase("boolean")) {
			return "false";
		}
		if (tipo.equals("String")) {
			return "\"\"";
		}
		return "null";
	}

	public void registrarImports(String classe) {
		if (classe.contains("."))
			listaImports.add("import " + classe + ";");
	}

	public static void main(String[] args) {
		BuilderMaster master = new BuilderMaster();
		master.gerarCodigoClasse(Conta.class);
	}
}