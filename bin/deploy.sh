#!/bin/bash

#------------------------------------------------
# 发布脚本：校验前置条件后执行 Maven 发布
#
# 用法: ./bin/deploy.sh [--dry-run]
#   --dry-run  只执行到 verify（完成 GPG 签名），不 deploy，用于发布前验证签名链路
#
# 说明: 本脚本不会自动打 tag、不会自动 push，这两步请手工完成
#------------------------------------------------

set -euo pipefail

# 切换到项目根目录，保证从任意路径调用都能正常工作
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${SCRIPT_DIR}/.."

DRY_RUN=false
for arg in "$@"; do
	case "${arg}" in
		--dry-run)
			DRY_RUN=true
			;;
		*)
			echo "ERROR: 未知参数 ${arg}" >&2
			echo "用法: $0 [--dry-run]" >&2
			exit 1
			;;
	esac
done

fail() {
	echo "ERROR: $1" >&2
	exit 1
}

# 1. 当前 JDK 必须是 17 及以上，发布链路只在 17/21 验证过
JAVA_BIN="$(command -v java || true)"
if [ -n "${JAVA_HOME:-}" ] && [ -x "${JAVA_HOME}/bin/java" ]; then
	JAVA_BIN="${JAVA_HOME}/bin/java"
fi
[ -n "${JAVA_BIN}" ] || fail "未找到 java，请先安装 JDK 17 及以上版本"

JAVA_VERSION="$("${JAVA_BIN}" -version 2>&1 | awk -F'"' '/version/ { print $2; exit }')"
JAVA_MAJOR="$(echo "${JAVA_VERSION}" | awk -F. '{ if ($1 == 1) print $2; else print $1 }')"
case "${JAVA_MAJOR}" in
	''|*[!0-9]*)
		fail "无法解析当前 Java 版本: ${JAVA_VERSION}"
		;;
esac
if [ "${JAVA_MAJOR}" -lt 17 ]; then
	fail "当前 JDK 版本为 ${JAVA_VERSION}，发布需要 JDK 17 及以上（推荐 17 或 21）"
fi

# 2. 工作区必须干净（包括未跟踪文件），避免发布内容与 tag 对不上
[ -d .git ] || fail "当前目录不是 git 仓库根目录: $(pwd)"
if [ -n "$(git status --porcelain)" ]; then
	echo "ERROR: 工作区不干净，请先提交或清理以下改动（包括未跟踪文件）：" >&2
	git status --short >&2
	exit 1
fi

# 3. 项目版本不能包含 SNAPSHOT
[ -f pom.xml ] || fail "未找到 pom.xml: $(pwd)/pom.xml"
PROJECT_VERSION="$(awk '
	/<parent>/ { inParent = 1 }
	/<\/parent>/ { inParent = 0; next }
	!inParent && /<version>/ {
		sub(/.*<version>/, "")
		sub(/<\/version>.*/, "")
		print
		exit
	}
' pom.xml)"
[ -n "${PROJECT_VERSION}" ] || fail "无法从 pom.xml 解析项目版本"
case "${PROJECT_VERSION}" in
	*SNAPSHOT*)
		fail "项目版本 ${PROJECT_VERSION} 包含 SNAPSHOT，不能发布"
		;;
esac

# 4. 同名 git tag 不能已存在
TAG="v${PROJECT_VERSION}"
if git rev-parse -q --verify "refs/tags/${TAG}" >/dev/null; then
	fail "git tag ${TAG} 已存在，请先确认版本号或删除旧 tag"
fi

echo ">>> 当前 JDK: ${JAVA_VERSION}"
echo ">>> 项目版本: ${PROJECT_VERSION}（发布 tag: ${TAG}）"

if [ "${DRY_RUN}" = true ]; then
	echo ">>> dry-run: 执行到 verify，完成签名验证，不会 deploy 到 Central"
	./mvnw -B -Prelease clean verify
	echo ">>> dry-run 结束，GPG 签名链路验证通过"
else
	echo ">>> 发布提示: release profile 中 autoPublish=false、waitUntil=published，"
	echo ">>> deploy 后会在 Central Portal 等待人工确认发布，命令会阻塞一段时间，请勿中断。"
	./mvnw -B -Prelease clean deploy
	echo ">>> 发布流程结束，请手工打 tag 并 push: git tag ${TAG} && git push origin ${TAG}"
fi
