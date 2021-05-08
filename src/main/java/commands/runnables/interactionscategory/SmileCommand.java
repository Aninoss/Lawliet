package commands.runnables.interactionscategory;

import java.util.Locale;
import commands.listeners.CommandProperties;
import commands.runnables.RolePlayAbstract;

@CommandProperties(
        trigger = "smile",
        emoji = "\uD83D\uDE04",
        executableWithoutArgs = true,
        aliases = { "happy" }
)
public class SmileCommand extends RolePlayAbstract {

    public SmileCommand(Locale locale, String prefix) {
        super(locale, prefix, false,
                "https://cdn.discordapp.com/attachments/736256980020101160/736256987121188924/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736256993723023430/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736256999389397047/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257012870021231/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257019052425266/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257024521928806/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257030448480265/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257032654684180/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257038002290728/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257041190092800/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257043983499264/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257049079316550/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257055605784676/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257060391354368/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257064053243985/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257068087902310/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257073754669166/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257082977812530/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257088401178706/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257091492380702/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257099679400097/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257111196958780/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257116511141948/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257121120681991/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257126615351396/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257134488059938/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257144374034472/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257149281370283/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257153475543201/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257160853454848/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257168273178725/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257173595881482/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257185771815052/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257193539665953/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257197532643328/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257204767817778/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257208488034385/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257214565843066/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257218650832967/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257226980982855/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257234643976283/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257238825697361/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257246014603314/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257250280210472/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257255997177936/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257261684523018/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257267485245591/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257276335358012/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257281997406238/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257290201464922/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257294139916418/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257304453840916/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257313190445107/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257318769000588/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257328088612944/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257349651660870/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257352902377482/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257359520858195/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257367011753984/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257380240719882/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257396166361088/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257406345936906/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257415825195108/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257425551655002/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257434217218108/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257443532636241/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257451543756810/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257458594512976/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257463141007452/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736257468317040680/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736258026385702973/happy.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736258137559924806/happy.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/736258306334654604/happy.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/743053465261179002/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/755840479375131064/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834813275715141662/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839348176093198/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839364449206332/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839376898424842/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839389217620039/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839402781605888/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839416987582464/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839432489992282/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839445647654932/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839458225586186/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839472834740232/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839487904874556/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839520364986448/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839538102566932/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839554007236608/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839569743478835/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839585901117460/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839601192894465/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839619190521887/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839634751651840/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839649667121202/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839664266707004/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839678606639144/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839721359310898/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839735549034566/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839750371180564/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839765135654962/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839783732936714/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839797801549895/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839811344302171/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839825470717952/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839842529345597/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839855741009960/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839868601139210/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839882802659388/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839896774017054/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839909465456651/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839922673320016/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839938729246720/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839953996251146/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839966819287090/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839981935427584/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834839995314602035/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840007730135090/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840019691634738/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840032480198666/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840044954583071/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840060301017118/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840074671620198/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840087224909854/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840099330064384/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840115716948038/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840127732711504/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840141641678889/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840156280193066/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840168388100146/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840180677410837/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840193784610846/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840208994074634/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840220393799720/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840232205615104/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840243864862740/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840255571689572/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840267341037649/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840284332163103/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840298064314450/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840309841133618/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840321451622400/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840333451788398/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840345211961404/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840357401002025/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840368460988456/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840381170253894/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840391966392330/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840406923542548/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840419363061810/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840431791570984/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840442976862228/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840454863388732/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840466032689223/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840477299638292/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840487923679282/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840498509971456/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840511020925009/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840524137037864/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840538322567238/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840552372961320/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840565342273576/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840588343181332/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840600917311558/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840614196477972/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840627001819166/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840638537072750/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840650976985108/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840664030183484/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/834840675048095775/smile.gif",
                "https://cdn.discordapp.com/attachments/736256980020101160/839471580995125288/smile.gif"
        );
    }

}