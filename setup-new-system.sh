#!/bin/bash

echo "========================================"
echo "RepoLens AI - New System Setup"
echo "========================================"
echo ""

echo "This script will help you set up RepoLens AI on a new system"
echo ""

# Check Java
echo "[1/5] Checking Java installation..."
if command -v java &> /dev/null; then
    echo "✅ Java is installed"
    java -version
else
    echo "❌ Java is NOT installed"
    echo ""
    echo "Please install Java 17 or higher:"
    echo "  Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "  Mac: brew install openjdk@17"
    echo "  Or download from: https://adoptium.net/"
    echo ""
    exit 1
fi
echo ""

# Check JAVA_HOME
echo "[2/5] Checking JAVA_HOME..."
if [ -z "$JAVA_HOME" ]; then
    echo "⚠️  JAVA_HOME is not set"
    echo ""
    echo "Please set JAVA_HOME in your shell profile:"
    echo "  Linux: export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
    echo "  Mac: export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home"
    echo ""
    echo "Add to ~/.bashrc or ~/.zshrc and run: source ~/.bashrc"
    echo ""
else
    echo "✅ JAVA_HOME is set to: $JAVA_HOME"
fi
echo ""

# Check Git
echo "[3/5] Checking Git installation..."
if command -v git &> /dev/null; then
    echo "✅ Git is installed"
    git --version
else
    echo "❌ Git is NOT installed"
    echo ""
    echo "Please install Git:"
    echo "  Ubuntu/Debian: sudo apt install git"
    echo "  Mac: brew install git"
    echo ""
    exit 1
fi
echo ""

# Check OCI Config
echo "[4/5] Checking OCI configuration..."
if [ -f "OCI_ApiKey/config" ]; then
    echo "✅ OCI config file found"
else
    echo "⚠️  OCI config file NOT found"
    echo ""
    echo "Please create OCI_ApiKey/config file:"
    echo "  1. Copy OCI_ApiKey/config.example to OCI_ApiKey/config"
    echo "  2. Update with your OCI credentials"
    echo "  3. Place your private key (.pem) in OCI_ApiKey/ directory"
    echo ""
    echo "See INSTALLATION_GUIDE.md for detailed instructions"
    echo ""
fi
echo ""

# Check application.yml
echo "[5/5] Checking application configuration..."
if [ -f "src/main/resources/application.yml" ]; then
    echo "✅ application.yml found"
    echo ""
    echo "⚠️  Please verify your compartment ID in application.yml"
    echo "   Location: src/main/resources/application.yml"
    echo "   Look for: spring.ai.oci.genai.compartment-id"
    echo ""
else
    echo "⚠️  application.yml NOT found"
    echo ""
    echo "Please copy application-example.yml to application.yml:"
    echo "  cp src/main/resources/application-example.yml src/main/resources/application.yml"
    echo ""
    echo "Then update with your OCI compartment ID"
    echo ""
fi
echo ""

echo "========================================"
echo "Setup Check Complete!"
echo "========================================"
echo ""

echo "Next Steps:"
echo "1. If any checks failed, follow the instructions above"
echo "2. Set up OCI credentials (see INSTALLATION_GUIDE.md)"
echo "3. Make scripts executable: chmod +x *.sh"
echo "4. Run the application: ./run.sh"
echo "5. Test the demo: ./test-demo.sh"
echo "6. Open browser: http://localhost:8080"
echo ""

echo "Documentation:"
echo "- INSTALLATION_GUIDE.md - Complete setup instructions"
echo "- README.md - Project overview"
echo "- QUICK_START.md - Quick start guide"
echo ""
