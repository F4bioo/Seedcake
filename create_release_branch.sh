#!/bin/bash

# Caminho para o arquivo Config.kt
CONFIG_FILE="buildSrc/src/main/java/Config.kt"

# Verificar se o arquivo Config.kt existe
if [[ ! -f $CONFIG_FILE ]]; then
    echo "Arquivo Config.kt não encontrado."
    exit 1
fi

# Ler o arquivo Config.kt e extrair os valores de versionCode e versionName
versionCode=$(grep "const val versionCode" $CONFIG_FILE | awk '{print $5}')
versionName=$(grep "const val versionName" $CONFIG_FILE | awk '{print $5}' | tr -d \")

# Verificar se os valores foram extraídos corretamente
if [[ -z "$versionCode" || -z "$versionName" ]]; then
    echo "Não foi possível extrair versionCode ou versionName do arquivo Config.kt."
    exit 1
fi

# Gerar novo versionCode e versionName
newVersionCode=$((versionCode + 1))
current_date=$(date +"%Y.%m.%d")
newVersionName="$current_date"

# Atualizar Config.kt com novos valores
sed -i.bak "s/const val versionCode: Int = $versionCode/const val versionCode: Int = $newVersionCode/" $CONFIG_FILE
sed -i.bak "s/const val versionName: String = \"$versionName\"/const val versionName: String = \"$newVersionName\"/" $CONFIG_FILE

# Remover arquivo backup criado pelo sed
rm -f "${CONFIG_FILE}.bak"

# Criar nova branch e fazer o push
git checkout -b release/${newVersionName}_${newVersionCode}
git add $CONFIG_FILE
git commit -m "Added: Create release branch v$newVersionName build $newVersionCode"
git push origin release/${newVersionName}_${newVersionCode}

echo "Branch release/${newVersionName}_${newVersionCode} criada e push feito com sucesso."
