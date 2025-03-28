{pkgs}: {
  deps = [
    pkgs.postgresql
    pkgs.jq
    pkgs.jdk
    pkgs.maven
  ];
}
